package projeto.spring.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import projeto.spring.Model.CancelamentoMatriculaEstado;
import projeto.spring.workflow.CancelamentoWorkflow;

@RestController
@RequestMapping("/matricula/cancelar")
public class CancelamentoMatriculaController {

    @Autowired
    private CancelamentoWorkflow workflow;

    @PutMapping("/solicitar/{rm}/{idDisciplina}")
    public ResponseEntity<CancelamentoMatriculaEstado> solicitarCancelamento(@PathVariable String rm, @PathVariable String idDisciplina) {
        return ResponseEntity.ok(workflow.solicitar());
    }

    @PutMapping("/aprovar/coordenador/{rm}/{idDisciplina}")
    public ResponseEntity<CancelamentoMatriculaEstado> aprovarCoordenador(@PathVariable String rm, @PathVariable String idDisciplina) {
        return ResponseEntity.ok(workflow.aprovarCoordenador());
    }

    @PutMapping("/aprovar/secretaria/{rm}/{idDisciplina}")
    public ResponseEntity<CancelamentoMatriculaEstado> aprovarSecretaria(@PathVariable String rm, @PathVariable String idDisciplina) {
        return ResponseEntity.ok(workflow.aprovarSecretaria());
    }
}

