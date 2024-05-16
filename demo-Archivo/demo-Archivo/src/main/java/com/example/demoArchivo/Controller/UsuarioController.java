
package com.example.demoArchivo.Controller;

import com.example.demoArchivo.Model.Usuario;
import com.example.demoArchivo.Service.IUsuarioService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1")
public class UsuarioController {
    
    @Autowired 
    private IUsuarioService usuarioServ;
    
    /*@Autowired
    private SessionRegistry sessionRegistry;*/
    //Endpoint para probar spring security
    @GetMapping("/index")
    public String index(){
    return "Hola MUNDO esta pagina esta ASEGURADA";
    }
    
    @GetMapping("/index2")
    public String index2(){
    return "Hola, esta pagina NO ESTA ASEGURADA!";
    }
    
    /*@GetMapping("/session")
    public ResponseEntity<?> getDetailsSession(){
        
        String sessionId = "";
        User user0bject = null;
        
       List<Object> sessions = sessionRegistry.getAllPrincipals();
       
       for(Object ses :sessions){
       if(ses instanceof User){
       user0bject = (User) ses;
       }
       
       List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(ses, false);
       
       for(SessionInformation sesin : sessionInformations){
           sessionId = sesin.getSessionId();
       }
       
       }
       
       Map<String,Object> response = new HashMap<>();
       response.put("response", "Hello World");
       response.put("sessionId", sessionId);
       response.put("sessionUser",user0bject);
       
    return ResponseEntity.ok(response);
    }*/
    
    @GetMapping ("/usuarios/traer")
    public List<Usuario> getUsuarios(){
    return usuarioServ.getUsuarios();
    }
    
    @PostMapping ("/usuarios/crear")
    public String crearUsuarios(@RequestBody Usuario usu){
    usuarioServ.saveUsuario(usu);
    return "El usuario fue creado con exito";
    }
    
    @DeleteMapping ("/usuarios/eliminar/{id}")
    public String deletePersona(@PathVariable Long id){
    usuarioServ.deleteUsuario(id);
    return "El usuario fue eliminado correctamente";
    }
    
    @PutMapping ("usuarios/modificar/{id_modificar}")
    public Usuario editUsuario(@PathVariable Long id_original,
                               @RequestParam(required=false, name = "id" )Long nueva_id,
                               @RequestParam(required=false, name = "correo" )String nuevoCorreo,
                               @RequestParam(required=false, name = "contrasenia" )String nuevoContrasenia,
                               @RequestParam(required=false, name = "nombre" )String nuevoNombre,
                               @RequestParam(required=false, name = "nombreRol" )String nuevoRol,
                               @RequestParam(required=false, name = "permisoCrear" )boolean permiso_crear,
                               @RequestParam(required=false, name = "permisoModificar" )boolean permiso_modificar,
                               @RequestParam(required=false, name = "permisoEliminar" )boolean permiso_eliminar,
                               @RequestParam(required=false, name = "permisoDescargar" )boolean permiso_descargar){
    
        usuarioServ.editUsuario(id_original, nueva_id, nuevoCorreo, nuevoContrasenia, nuevoNombre, nuevoRol, permiso_crear, permiso_modificar, permiso_eliminar, permiso_descargar);
        Usuario usu = usuarioServ.findUsuario(nueva_id);
        return usu;
    
    }
    
    
}
