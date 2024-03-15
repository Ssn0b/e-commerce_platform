package app.snob.ecommerce_platform.controller;

import app.snob.ecommerce_platform.service.Impl.PaypalServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaypalServiceImpl payPalService;
    @PostMapping
    public Map<String, Object> makePayment(Principal principal, @RequestParam("orderId") Long orderId){
        return payPalService.createPayment(principal.getName(),orderId);
    }
    @GetMapping(value = "/complete")
    public ResponseEntity<String> completePayment(HttpServletRequest request){
        return payPalService.completePayment(request);
    }
    @GetMapping(value = "/cancel")
    public ResponseEntity<String> cancelPayment(HttpServletRequest request){
        payPalService.cancelPayment(request);
        return ResponseEntity.ok("Payment canceled");
    }
}
