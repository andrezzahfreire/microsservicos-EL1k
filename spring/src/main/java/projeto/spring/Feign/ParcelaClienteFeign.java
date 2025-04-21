package projeto.spring.Feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "parcela", url = "http://localhost:8080/parcela")
public interface ParcelaClienteFeign {

    @GetMapping("/cidadao/{cidadaoId}/total-devido")
    public Double getTotalDevido(@PathVariable("cidadaoId") Long cidadaoId);

    @GetMapping("/cidadao/{cidadaoId}/total-pagas")
    public Long getTotalPagas(@PathVariable("cidadaoId") Long cidadaoId);

    @PostMapping("/pagar/{id}")
    public String pagarParcela(@PathVariable("id") Long id);
}
