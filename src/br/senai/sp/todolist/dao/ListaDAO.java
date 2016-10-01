package br.senai.sp.todolist.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import br.senai.sp.todolist.modelo.ItemLista;
import br.senai.sp.todolist.modelo.Lista;

@Repository
public class ListaDAO {

	@PersistenceContext
	private EntityManager manager;
	
	@Transactional // Manter as transactional nos métodos da DAO
	public void inserir(Lista lista){
		manager.persist(lista);
	}
	
	// Não necessita de @Transactional pois essa query não irá afetar os registros no banco de dados.
	// 
	public List<Lista> listar(){
		TypedQuery<Lista> query = manager.createQuery("select l from Lista l", Lista.class);
		return query.getResultList();		
	}
	
	@Transactional
	public void excluir(long idLista){
		Lista lista = manager.find(Lista.class, idLista);
		manager.remove(lista);
	}
	
	@Transactional
	public void excluirItem(long idItem){
		ItemLista item = manager.find(ItemLista.class, idItem);
		Lista lista = item.getLista();
		lista.getItens().remove(item);
		manager.merge(lista);
		
	}
	
	// Não necessita de @Transactional pois essa query não irá afetar os registros no banco de dados.
	// 
	public Lista buscarLista(long idLista){
		Lista lista = manager.find(Lista.class, idLista);
		return lista;		
	}
}
