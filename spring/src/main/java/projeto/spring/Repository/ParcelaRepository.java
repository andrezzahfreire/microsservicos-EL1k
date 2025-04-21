package projeto.spring.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import projeto.spring.Model.ParcelaModel;

public interface ParcelaRepository extends CrudRepository<ParcelaModel, Integer> {
    List<ParcelaModel> findByCidadaoId(Integer cidadaoId);
    List<ParcelaModel> findByCidadaoIdAndPagaTrue(Integer cidadaoId);
}


