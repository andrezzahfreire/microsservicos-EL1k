package projeto.spring.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import projeto.spring.Model.CidadaoModel;

@Repository
public interface CidadaoRepository extends CrudRepository<CidadaoModel,Integer>{
    
}
