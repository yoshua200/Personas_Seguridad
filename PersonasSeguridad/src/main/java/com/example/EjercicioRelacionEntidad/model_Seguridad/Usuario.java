package com.example.EjercicioRelacionEntidad.model_Seguridad;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="USERS")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID_USER")
    private Long id;

    @Column(name="USERNAME", unique = true)
    @NotBlank
    private String username;

    @Column(name="PSW")
    @NotBlank
    /*Con la anotacion JsonProperty se puede especificar que este campo de la entidad
     * solo sea accesible cuando se escribe en el, pero no para cuando se lee
     * Asi cuando se haga una consulta de lista de usuarios, el campo psw
     * no se vera en el JSON*/
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String psw;

    @Column(name="ENABLED")
    private boolean enabled;

    /*Al ser una relacion de muchos a muchos necesitamos hacer la asociasion
     * en este caso solo sera de usuario a rol, es decir, unidireccional
     * */
    @JsonIgnoreProperties({"usuarios","handler","hibernateLazyInitializer"})
    @ManyToMany
    @JoinTable(
            name = "ROLES_DE_USUARIOS",//el nombre de la tabla intermedia en la BD
            joinColumns = @JoinColumn(name = "USUARIO_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROL_ID"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"USUARIO_ID","ROL_ID"})}
    )
    private List<ROL> roles;


    /*Este no es un campo mapeado a la tabla de la BD
     * solo se usa para el objeto en el back, por lo
     * que se usa la anotacion Transient para indicar
     * a Java que no es un campo mapeado a la tabla.*/

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin; //Lo excluimos del JSON cuando devuelva datos :3


    /*No es necesario mapearlo a la BD debido a que
     * en la BD por defecto tiene el valor de 1*/
    private boolean habilitado;


    /*Nota, si se usa el tipo de dato primitivo boolean
     * si no se asigna un valor en la creacion, por defecto este valor es false
     *
     * Pero si se usa el tipo wrapper Boolean, esto deja la instancia nula y arrojara
     * error en el save()*/


    public void prePersist(){
        enabled = true;
    }


}
