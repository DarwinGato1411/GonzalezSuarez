package com.ec.entidad;

import com.ec.entidad.Predio;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-07-09T05:45:18")
@StaticMetamodel(Propietario.class)
public class Propietario_ { 

    public static volatile SingularAttribute<Propietario, String> propLocal;
    public static volatile SingularAttribute<Propietario, String> porpCedula;
    public static volatile SingularAttribute<Propietario, Date> porpFechaNac;
    public static volatile CollectionAttribute<Propietario, Predio> predioCollection;
    public static volatile SingularAttribute<Propietario, Integer> idPropietario;
    public static volatile SingularAttribute<Propietario, Integer> propEdad;
    public static volatile SingularAttribute<Propietario, String> propNombre;
    public static volatile SingularAttribute<Propietario, String> propApellido;
    public static volatile SingularAttribute<Propietario, String> propDireccion;
    public static volatile SingularAttribute<Propietario, String> propSector;

}