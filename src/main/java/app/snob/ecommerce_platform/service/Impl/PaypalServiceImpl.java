package app.snob.ecommerce_platform.service.Impl;

import app.snob.ecommerce_platform.entity.OrderDetails;
import app.snob.ecommerce_platform.entity.OrderItems;
import app.snob.ecommerce_platform.entity.ProductInventory;
import app.snob.ecommerce_platform.entity.User;
import app.snob.ecommerce_platform.entity.enums.Status;
import app.snob.ecommerce_platform.exception.InvalidEmailException;
import app.snob.ecommerce_platform.repository.OrderDetailsRepository;
import app.snob.ecommerce_platform.repository.ProductInventoryRepository;
import app.snob.ecommerce_platform.repository.UserRepository;
import app.snob.ecommerce_platform.service.PaypalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaypalServiceImpl implements PaypalService {
    @Value("${paypal.clientId}")
    private String clientId;
    @Value("${paypal.secret}")
    private String clientSecret;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ProductInventoryRepository productInventoryRepository;

    @Override
    public Map<String, Object> createPayment(String email, Long orderId) {
        log.info("Creating payment for order ID: {}", orderId);
        Optional<OrderDetails> orderDetails = orderDetailsRepository.findById(orderId);
        if (orderDetails.isEmpty()) {
            log.error("EntityNotFoundException at createPayment method");
            throw new EntityNotFoundException("Order details with ID "  + orderId + " not found");
        }
        if (!orderDetails.get().getUser().getEmail().equals(email)) {
            log.error("InvalidEmailException at createPayment method");
            throw new InvalidEmailException("Email does not matches");
        }
        Map<String, Object> response = new HashMap<String, Object>();
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(String.valueOf(orderDetails.get().getTotal()));
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/api/v1/payment/cancel?orderId=" + orderId);
        redirectUrls.setReturnUrl("http://localhost:8080/api/v1/payment/complete?orderId=" + orderId);
        payment.setRedirectUrls(redirectUrls);
        Payment createdPayment;
        try {
            String redirectUrl = "";
            APIContext context = new APIContext(clientId, clientSecret, "sandbox");
            createdPayment = payment.create(context);
            if (createdPayment != null) {
                List<Links> links = createdPayment.getLinks();
                for (Links link : links) {
                    if (link.getRel().equals("approval_url")) {
                        redirectUrl = link.getHref();
                        break;
                    }
                }
                response.put("status", "success");
                response.put("redirect_url", redirectUrl);
            }
        } catch (PayPalRESTException e) {
            log.error("Error happened during payment creation!");
        }
        return response;
    }

    @Override
    public ResponseEntity<String> completePayment(HttpServletRequest req) {
        log.info("Completing payment for order ID: {}", req.getParameter("orderId"));
        String orderId = req.getParameter("orderId");
        Optional<OrderDetails> orderDetailsOptional = orderDetailsRepository.findById(Long.valueOf(orderId));

        if (orderDetailsOptional.isPresent()) {
            OrderDetails orderDetails = orderDetailsOptional.get();
            orderDetails.setStatus(Status.PROCESSED);
            orderDetailsRepository.save(orderDetails);
            for (OrderItems orderItem : orderDetails.getOrderDetailsList()) {
                ProductInventory productInventory = orderItem.getProduct().getInventory();
                Integer updatedQuantity = productInventory.getQuantity() - orderItem.getQuantity();
                productInventory.setQuantity(updatedQuantity);
                productInventoryRepository.save(productInventory);
            }
        }
        Map<String, Object> response = new HashMap<>();
        Payment payment = new Payment();
        payment.setId(req.getParameter("paymentId"));

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(req.getParameter("PayerID"));
        try {
            APIContext context = new APIContext(clientId, clientSecret, "sandbox");
            Payment createdPayment = payment.execute(context, paymentExecution);
            if (createdPayment != null) {
                log.info(createdPayment.toJSON());
                response.put("status", "success");
                response.put("payment", createdPayment);
            }
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }
        return ResponseEntity.ok(response.toString());
    }
    @Override
    public void cancelPayment(HttpServletRequest req) {
        log.info("Cancelling payment for order ID: {}", req.getParameter("orderId"));
        String orderId = req.getParameter("orderId");
        Optional<OrderDetails> orderDetailsOptional = orderDetailsRepository.findById(Long.valueOf(orderId));
        if (orderDetailsOptional.isPresent()) {
            OrderDetails orderDetails = orderDetailsOptional.get();
            orderDetails.setStatus(Status.CANCELED);
        }
    }
}
