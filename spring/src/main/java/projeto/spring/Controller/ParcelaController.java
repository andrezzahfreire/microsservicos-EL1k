package projeto.spring.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import projeto.spring.Model.CidadaoModel;
import projeto.spring.Model.ParcelaModel;
import projeto.spring.Repository.CidadaoRepository;
import projeto.spring.Repository.ParcelaRepository;

@RestController
@RequestMapping("/parcela")
public class ParcelaController {

    @Autowired
    private ParcelaRepository parcelaRepo;

    @Autowired
    private CidadaoRepository cidadaoRepo;

    @PostMapping("/gerar/{cidadaoId}")
    public ResponseEntity<String> gerarParcelas(@PathVariable Integer cidadaoId, @RequestParam boolean pagamentoUnico) {
        if (cidadaoRepo.findById(cidadaoId).isEmpty()) {
            return new ResponseEntity<>("Cidadão não encontrado", HttpStatus.NOT_FOUND);
        }

        CidadaoModel cidadao = cidadaoRepo.findById(cidadaoId).get();
        List<ParcelaModel> parcelas = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            ParcelaModel parcela = new ParcelaModel();
            parcela.setNumeroParcela(i);
            parcela.setPaga(false);
            parcela.setCidadao(cidadao);

            if (pagamentoUnico) {
                parcela.setValor(i == 1 ? 1000.00 : 0.00);
            } else {
                parcela.setValor(1000.00);
            }

            parcelas.add(parcela);
        }

        parcelaRepo.saveAll(parcelas);
        return new ResponseEntity<>("Parcelas geradas com sucesso!", HttpStatus.OK);
    }

    @PostMapping("/pagar/{id}")
    public ResponseEntity<String> pagarParcela(@PathVariable Integer id) {
        if (parcelaRepo.findById(id).isEmpty()) {
            return new ResponseEntity<>("Parcela não encontrada", HttpStatus.NOT_FOUND);
        }

        ParcelaModel parcela = parcelaRepo.findById(id).get();
        parcela.setPaga(true);
        parcelaRepo.save(parcela);

        return new ResponseEntity<>("Parcela paga com sucesso!", HttpStatus.OK);
    }

    @GetMapping("/cidadao/{cidadaoId}/total-devido")
    public ResponseEntity<Double> getTotalDevido(@PathVariable Integer cidadaoId) {
        if (cidadaoRepo.findById(cidadaoId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<ParcelaModel> parcelas = parcelaRepo.findByCidadaoId(cidadaoId);
        double total = parcelas.stream()
                .filter(p -> !p.isPaga())
                .mapToDouble(ParcelaModel::getValor)
                .sum();

        return new ResponseEntity<>(total, HttpStatus.OK);
    }

    @GetMapping("/cidadao/{cidadaoId}/total-pagas")
    public ResponseEntity<Long> getTotalPagas(@PathVariable Integer cidadaoId) {
        if (cidadaoRepo.findById(cidadaoId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        long totalPagas = parcelaRepo.findByCidadaoIdAndPagaTrue(cidadaoId).size();
        return new ResponseEntity<>(totalPagas, HttpStatus.OK);
    }

    @GetMapping("/cidadao/{cidadaoId}/listar")
    public ResponseEntity<List<ParcelaModel>> listarParcelasPorCidadao(@PathVariable Integer cidadaoId) {
        if (cidadaoRepo.findById(cidadaoId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<ParcelaModel> parcelas = parcelaRepo.findByCidadaoId(cidadaoId);
        return new ResponseEntity<>(parcelas, HttpStatus.OK);
    }
}
