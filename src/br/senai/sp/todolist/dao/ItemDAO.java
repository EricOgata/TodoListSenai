package br.senai.sp.todolist.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.senai.sp.todolist.modelo.ItemLista;
import br.senai.sp.todolist.modelo.Lista;

@Repository
public class ItemDAO {
	@PersistenceContext
	private EntityManager manager;
	
	@Transactional
	public void marcarFeito(Long idItem, boolean valor){
		ItemLista item = manager.find(ItemLista.class, idItem);
		item.setFeito(valor);
		manager.merge(item);
	}
	
	@Transactional
	public void inserir(long idLista, ItemLista item){
		item.setLista(manager.find(Lista.class, idLista));
		manager.persist(item);
	}
	
	public ItemLista buscarItem(long idItem){
		ItemLista itemLista = manager.find(ItemLista.class, idItem);
		return itemLista;
	}
}
