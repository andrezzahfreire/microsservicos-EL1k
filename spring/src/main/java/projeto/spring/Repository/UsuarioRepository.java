package projeto.spring.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import projeto.spring.Model.UsuarioModel;

@Repository
public interface UsuarioRepository extends CrudRepository<UsuarioModel,Integer>{
    Optional<UsuarioModel> findByCidadaoId(Integer cidadaoId);
}
