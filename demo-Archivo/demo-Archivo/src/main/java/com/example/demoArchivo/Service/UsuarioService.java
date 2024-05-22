
package com.example.demoArchivo.Service;

import com.example.demoArchivo.Model.Usuario;
import com.example.demoArchivo.Repository.IUsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements IUsuarioService{
    @Autowired
    private IUsuarioRepository usuarioRepo;

    @Override
    public List<Usuario> getUsuarios() {
    
        List<Usuario>listaUsuarios = usuarioRepo.findAll();
        return listaUsuarios;
    }

    @Override
    public void saveUsuario(Usuario usu) {
        
        usuarioRepo.save(usu);    
    }

    @Override
    public void deleteUsuario(Long id) {
    
        usuarioRepo.deleteById(id);
    }

    @Override
    public Usuario findUsuario(Long id) {
    //busco el objeto original
       Usuario usu = usuarioRepo.findById(id).orElse(null);
       return usu;
    }
    //proceso de modificacion a nivel logico
    @Override
    public void editUsuario(Long idOriginal, Long idNueva, String nuevoCorreo, String nuevoContrasenia, String nuevoNombre, String nuevoRol, boolean nuevoPermisoCrear, boolean nuevoPermisoModificar, boolean nuevoPermisoEliminar, boolean nuevoPermisoDescargar) {
    
        Usuario usu =this.findUsuario(idOriginal);
        usu.setId(idNueva);
        usu.setContrasenia(nuevoContrasenia);
        usu.setCorreo(nuevoCorreo);
        usu.setNombre(nuevoNombre);
        usu.setNombre_rol(nuevoRol);
        usu.setPermiso_crear(nuevoPermisoCrear);
        usu.setPermiso_descargar(nuevoPermisoDescargar);
        usu.setPermiso_eliminar(nuevoPermisoEliminar);
        usu.setPermiso_modificar(nuevoPermisoModificar);
        
        //guardar los cambios
        this.saveUsuario(usu);
        
    }
    
    
    
}
