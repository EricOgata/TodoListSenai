package br.senai.sp.todolist.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWTSigner;

import br.senai.sp.todolist.dao.ListaDAO;
import br.senai.sp.todolist.dao.UsuarioDAO;
import br.senai.sp.todolist.modelo.Usuario;

@RestController
public class UsuarioRestController {
	
	public static final String SECRET = "crifradoprograma"; // cifra para descriptografar.
	public static final String ISSUER = "Eric Ogata"; // emissor do token.
	
	@Autowired
	private UsuarioDAO usuarioDao;
	
	/**
	 * Adicionar Usuários
	 */
	@RequestMapping(
			value="/usuario/addUsuario",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_UTF8_VALUE
			)
	public ResponseEntity<Usuario> inserirUsuario(@RequestBody Usuario usuario){
		try{
			usuarioDao.inserir(usuario);
			URI location = new URI("/usuario/"+usuario.getId());
			return ResponseEntity.created(location).body(usuario);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	
	@RequestMapping(
			value="/login",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces=MediaType.APPLICATION_JSON_UTF8_VALUE
			)
	public ResponseEntity<String> logar(@RequestBody Usuario usuario){
		try {
			usuario = usuarioDao.logar(usuario);
			if (usuario!=null) {
				// Data de emissão do Token
				long iat = System.currentTimeMillis() / 1000;
				// Data de Expiração do token
				long exp = iat + 60;
				JWTSigner signer = new JWTSigner(SECRET);
				HashMap<String, Object> claims = new HashMap<>();
				claims.put("iat", iat);
				claims.put("exp", exp);
				claims.put("iss", ISSUER);
				claims.put("id_usuario", usuario.getId());
				// gerar Token
				String jwt = signer.sign(claims); // Método crpitografa toda a HashMap na forma de um Token
				JSONObject token = new JSONObject();
				token.put("token",jwt);
				return ResponseEntity.ok(token.toString());
				
			} else {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	/**
	 * Remover usuário
	 */
	@RequestMapping(
			value="/usuario/{idUsuario}",
			method=RequestMethod.DELETE)
	public ResponseEntity<Void> excluirUsuario(@PathVariable long idUsuario){
		usuarioDao.excluir(idUsuario);
		return ResponseEntity.noContent().build();
	}
	
	
	/**
	 * Listar Usuários
	 */	
	@RequestMapping(
			value="/usuario",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Usuario> listar(){
		return usuarioDao.listar();
	}
	
	/**
	 * Buscar Usuário
	 */
	
	@RequestMapping(
			value="/usuario/{idUsuario}",
			method=RequestMethod.GET,
			produces=MediaType.APPLICATION_JSON_UTF8_VALUE
			)
	public Usuario buscarUsuario(@PathVariable long idUsuario){
		Usuario usuario = usuarioDao.buscarUsuario(idUsuario);
		return usuario;
	}
}
