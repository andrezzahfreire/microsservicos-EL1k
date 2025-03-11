package projeto.spring.Model;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "Usuario")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "username", length = 255, nullable = false)
    private String username;

    @Column(name = "senha", length = 100, nullable = false)
    private String senha;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cidadao_id", referencedColumnName = "ID")
    private CidadaoModel cidadao;

    @Column(name = "tentativas_falhas")
    private Integer tentativas_falhas=0;

    @Column(name = "bloqueado")
    private boolean bloqueado=false;
    
    @Column(name = "ultimo_login")
    private Date ultimo_login=null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public CidadaoModel getCidadao() {
        return cidadao;
    }

    public void setCidadao(CidadaoModel cidadao) {
        this.cidadao = cidadao;
    }

    public Integer getTentativas_falhas() {
        return tentativas_falhas;
    }

    public void setTentativas_falhas(Integer tentativas_falhas) {
        this.tentativas_falhas = tentativas_falhas;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public Date getUltimo_login() {
        return ultimo_login;
    }

    public void setUltimo_login(Date ultimo_login) {
        this.ultimo_login = ultimo_login;
    }
    
    
}