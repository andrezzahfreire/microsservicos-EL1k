package projeto.spring.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CAD_CidadaoModel")
public class CidadaoModel {
    
    @Id
    @Column(name = "ID")
    private Integer id;
    
    @Column(name = "nome", length = 255, nullable = false)
    private String nome;
    
    @Column(name = "endereco", length = 255)
    private String endereco;
    
    @Column(name = "bairro", length = 255)
    private String bairro;

    @OneToOne(mappedBy = "cidadao")
    private UsuarioModel user;
    
    // Construtores
    public CidadaoModel() {}
    
    public CidadaoModel(Integer id, String nome, String endereco, String bairro) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.bairro = bairro;
    }
    
    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    @Override
    public String toString() {
        return "Cidadao{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                ", bairro='" + bairro + '\'' +
                '}';
    }
}
