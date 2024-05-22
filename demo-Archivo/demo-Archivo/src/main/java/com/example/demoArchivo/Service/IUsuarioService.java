
package com.example.demoArchivo.Service;

import com.example.demoArchivo.Model.Usuario;
import java.util.List;


public interface IUsuarioService {
    
    //metodo para traer a todos los usuarios
    //lectura
    public List<Usuario> getUsuarios();
    
    //alta
    public void saveUsuario(Usuario usu);
    
    //baja
    public void deleteUsuario(Long id);
    
    //lectura de un solo objeto
    public Usuario findUsuario(Long id);
    
    //modificacion
    public void editUsuario(Long idOriginal, Long idNueva, String nuevoCorreo,
                            String nuevoContrasenia, String nuevoNombre,
                            String nuevoRol,boolean nuevoPermisoCrear, 
                            boolean nuevoPermisoModificar, boolean nuevoPermisoEliminar,
                            boolean nuevoPermisoDescargar);
}
