package br.senai.sp.todolist.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.senai.sp.todolist.modelo.Usuario;

@Repository
public class UsuarioDAO {
	
	@PersistenceContext
	private EntityManager manager;
	
	/**
	 * Inserir Usuários
	 */
	@Transactional
	public void inserir(Usuario usuario){
		manager.persist(usuario);
	}
	
	/**
	 * 
	 */
	public Usuario logar(Usuario usuario){
		TypedQuery<Usuario> query = manager.createQuery("select u from Usuario u where u.login = :login and u.password = :password", Usuario.class);
		query.setParameter("login", usuario.getLogin());
		query.setParameter("password", usuario.getPassword());
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	/* Remover Usuário */
	@Transactional
	public void excluir(Long idUsuario){
		Usuario usuario = manager.find(Usuario.class, idUsuario);
		manager.remove(usuario);
	}
	
	
	/* Listar Usuário */
	public List<Usuario> listar(){
		TypedQuery<Usuario> query = manager.createQuery("select u from Usuario u", Usuario.class);
		return query.getResultList();
	}	
	
	public Usuario buscarUsuario(long idUsuario){
		Usuario usuario = manager.find(Usuario.class, idUsuario);
		return usuario;
	}
}
