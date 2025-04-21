package projeto.spring.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import projeto.spring.feign.ParcelaClienteFeign;

@Component
public class FeignTestRunner implements CommandLineRunner {

    @Autowired
    private ParcelaClienteFeign parcelaFeign;

    @Override
    public void run(String... args) throws Exception {

        // Teste para obter o total devido
        Long cidadaoId = 1L; // Exemplo de ID do cidadão
        Double totalDevido = parcelaFeign.getTotalDevido(cidadaoId);
        System.out.println("Total devido para o cidadão " + cidadaoId + ": " + totalDevido);

        // Teste para obter o total de parcelas pagas
        Long totalPagas = parcelaFeign.getTotalPagas(cidadaoId);
        System.out.println("Total de parcelas pagas para o cidadão " + cidadaoId + ": " + totalPagas);

        // Teste para pagar uma parcela (por exemplo, parcela 1)
        Long parcelaId = 1L;
        String response = parcelaFeign.pagarParcela(parcelaId);
        System.out.println("Resposta ao pagar a parcela: " + response);
    }
}
