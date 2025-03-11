package projeto.spring.Controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import projeto.spring.Model.CidadaoModel;
import projeto.spring.Model.UsuarioModel;
import projeto.spring.Repository.CidadaoRepository;
import projeto.spring.Repository.UsuarioRepository;

@RestController
@RequestMapping("/user")
public class UsuarioController {
        
    //crud usuario

    @Autowired
    private UsuarioRepository repo;
    @Autowired
    private CidadaoRepository cidadaoRepo;

    private static final String CHAVE_ACESSO_ADMIN = "admin";


    @GetMapping
    public ResponseEntity<List<UsuarioModel>> listarUsuario(){
        List<UsuarioModel> Usuario = (List<UsuarioModel> )repo.findAll();
        return new ResponseEntity<>(Usuario,HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<String> buscarUsuario(@PathVariable Integer id){
        if (!(repo.findById(id).isEmpty())) {
            UsuarioModel Usuario = repo.findById(id).get(); 
            return new ResponseEntity<>("Usuario Encontrado: " + Usuario.getUsername(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Usuario não encontrado!", HttpStatus.NOT_FOUND);
        }
        
    }

    
    @PostMapping
    public ResponseEntity<String> criarUsuario(@RequestBody UsuarioModel novaUsuario) {
    if (novaUsuario.getCidadao() == null || novaUsuario.getCidadao().getId() == null) {
        return new ResponseEntity<>("Erro: Cidadão inválido, ID do cidadão é necessário.", HttpStatus.BAD_REQUEST);
    }
    
    Optional<CidadaoModel> cidadaoExistente = cidadaoRepo.findById(novaUsuario.getCidadao().getId());
    if (cidadaoExistente.isEmpty()) {
        return new ResponseEntity<>("Erro: Cidadão não encontrado no sistema.", HttpStatus.BAD_REQUEST);
    }
    
    novaUsuario.setCidadao(cidadaoExistente.get());
    
    if (repo.findByCidadaoId(novaUsuario.getCidadao().getId()).isPresent()) {
        return new ResponseEntity<>("Erro: Já existe um usuário cadastrado para este cidadão.", HttpStatus.FORBIDDEN);
    }
    
    repo.save(novaUsuario);
    return new ResponseEntity<>("Usuário criado com sucesso: " + novaUsuario.getUsername(), HttpStatus.CREATED);
}


    
    @PutMapping("{id}")
    public ResponseEntity<UsuarioModel> atualizarUsuario(@PathVariable (required = true) Integer id, @RequestBody UsuarioModel novaUsuario){
        if (repo.findById(id).isEmpty()) {
            return new ResponseEntity<UsuarioModel>(new UsuarioModel(),HttpStatus.NOT_FOUND);
        }else{
            novaUsuario.setId(id);
            repo.save(novaUsuario);
            return new ResponseEntity<UsuarioModel>(novaUsuario,HttpStatus.OK);
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Integer id){
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return new ResponseEntity<>("Usuario Deletado " + id, HttpStatus.OK);
        }
        return new ResponseEntity<>("Erro ao deletar" + id, HttpStatus.NOT_FOUND);

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsuarioModel usuario) {
        Optional<UsuarioModel> usuarioExistente = repo.findById(usuario.getId());
    
        if (usuarioExistente.isEmpty()) {
            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
    
        UsuarioModel user = usuarioExistente.get();
    
        if (user.isBloqueado()) {
            return new ResponseEntity<>("Usuário bloqueado. Contate o suporte.", HttpStatus.FORBIDDEN);
        }
    
        if (user.getUltimo_login() != null) {
            long diffInMillis = new Date().getTime() - user.getUltimo_login().getTime();
            long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);
    
            if (diffInDays > 30) {
                return new ResponseEntity<>("Sua senha expirou. Por favor, troque a sua senha.", HttpStatus.FORBIDDEN);
            }
        }
    
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty() || !user.getSenha().equals(usuario.getSenha())) {
            user.setTentativas_falhas(user.getTentativas_falhas() + 1);
    
            if (user.getTentativas_falhas() >= 3) {
                user.setBloqueado(true);
                repo.save(user);
                return new ResponseEntity<>("Usuário bloqueado após 3 tentativas falhas", HttpStatus.FORBIDDEN);
            }
    
            repo.save(user);
            return new ResponseEntity<>("Senha incorreta. Tentativas restantes: " + (3 - user.getTentativas_falhas()), HttpStatus.UNAUTHORIZED);
        }
    
        user.setTentativas_falhas(0);
        user.setUltimo_login(new Date());
        repo.save(user);
    
        return new ResponseEntity<>("Login realizado com sucesso", HttpStatus.OK);
    }
    

    @PutMapping("/{id}/senha")
        public ResponseEntity<String> trocarSenha(@PathVariable Integer id, @RequestBody Map<String, String> senhaRequest) {
        Optional<UsuarioModel> usuarioExistente = repo.findById(id);

        // Verifica se o usuário existe
        if (usuarioExistente.isEmpty()) {
            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }

        UsuarioModel user = usuarioExistente.get();

        String senhaAtual = senhaRequest.get("senhaAtual");
        String novaSenha = senhaRequest.get("novaSenha");

        if (!user.getSenha().equals(senhaAtual)) {
            return new ResponseEntity<>("Senha atual incorreta", HttpStatus.UNAUTHORIZED);
        }

        user.setSenha(novaSenha);
        repo.save(user);

        return new ResponseEntity<>("Senha alterada com sucesso", HttpStatus.OK);
}

    @PostMapping("/desbloquear/{id}")
    public ResponseEntity<String> desbloquearUsuario(@PathVariable Integer id, @RequestParam String chave) {

        if (!CHAVE_ACESSO_ADMIN.equals(chave)) {
            return new ResponseEntity<>("Acesso não autorizado", HttpStatus.FORBIDDEN);
        }

        Optional<UsuarioModel> usuarioExistente = repo.findById(id);

        if (usuarioExistente.isEmpty()) {
            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }

        UsuarioModel user = usuarioExistente.get();

        if (!user.isBloqueado()) {
            return new ResponseEntity<>("Usuário já está desbloqueado", HttpStatus.OK);
        }

        user.setBloqueado(false);
        repo.save(user);

        return new ResponseEntity<>("Usuário desbloqueado com sucesso", HttpStatus.OK);
    }

  
}
