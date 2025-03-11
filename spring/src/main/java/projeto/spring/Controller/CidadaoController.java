package projeto.spring.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projeto.spring.Model.CidadaoModel;
import projeto.spring.Repository.CidadaoRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/cidadao")
public class CidadaoController {
    @Autowired
    private CidadaoRepository repo;

    @GetMapping
    public ResponseEntity<List<CidadaoModel>> listarCidadaos(){
        List<CidadaoModel> cidadaos = (List<CidadaoModel> )repo.findAll();
        return new ResponseEntity<>(cidadaos,HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<String> buscarCidadao(@PathVariable Integer id){
        if (!(repo.findById(id).isEmpty())) {
            CidadaoModel cidadao = repo.findById(id).get(); 
            return new ResponseEntity<>("cidadao Encontrado: " + cidadao.getNome(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("cidadao não encontrado!", HttpStatus.NOT_FOUND);
        }
        
    }

    @PostMapping 
    public ResponseEntity<String> criarCidadao(@RequestBody CidadaoModel novaDiscuplina){
        repo.save(novaDiscuplina);
        return new ResponseEntity<String>("cidadao Criado"+ novaDiscuplina.getNome(),HttpStatus.OK);
    }
    
    @PutMapping("{id}")
    public ResponseEntity<CidadaoModel> atualizarCidadao(@PathVariable (required = true) Integer id, @RequestBody CidadaoModel novacidadao){
        if (repo.findById(id).isEmpty()) {
            return new ResponseEntity<CidadaoModel>(new CidadaoModel(),HttpStatus.NOT_FOUND);
        }else{
            novacidadao.setId(id);
            repo.save(novacidadao);
            return new ResponseEntity<CidadaoModel>(novacidadao,HttpStatus.OK);
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletarCidadao(@PathVariable Integer id){
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return new ResponseEntity<>("cidadao Deletado " + id, HttpStatus.OK);
        }
        return new ResponseEntity<>("Erro ao deletar" + id, HttpStatus.NOT_FOUND);

    }
    
    
}
