package br.senai.sp.todolist.controller;

import java.net.URI;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sun.jndi.toolkit.url.Uri;

import br.senai.sp.todolist.dao.ItemDAO;
import br.senai.sp.todolist.modelo.ItemLista;

@RestController
public class ItemRestController {

	@Autowired
	private ItemDAO itemDao;
	
	
	/**
	 * 
	 * @param idItem @PathVariable informa que este parâmetro irá vir pelo caminho URL.
	 * @param strFeito @RequestBody informa que este parâmetro vem pelo body da requisição.
	 * @return
	 */
	
	@RequestMapping(
			value="/item/{idItem}",
			method=RequestMethod.PUT,
			consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Void> marcarFeito(@PathVariable long idItem,@RequestBody String strFeito){
			try{
				JSONObject job = new JSONObject(strFeito);
				itemDao.marcarFeito(idItem, job.getBoolean("feito"));
				HttpHeaders responseHeader = new HttpHeaders(); // novo cabeçalho http;
				URI location = new URI("/item/"+idItem); // Nova location do item.
				responseHeader.setLocation(location);
				return new ResponseEntity<>(responseHeader, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
	}
	
	@RequestMapping(
			value="/lista/{idLista}/item",
			method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ItemLista> addItem(@PathVariable long idLista,@RequestBody ItemLista item){
		try {
			itemDao.inserir(idLista, item);
			URI location = new URI("/item/"+item.getId());
			return ResponseEntity.created(location).body(item);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}
	
	@RequestMapping(
			value="/item/{idItem}",
			method=RequestMethod.GET,
			consumes=MediaType.APPLICATION_JSON_UTF8_VALUE
			)
	public ItemLista buscarItem(@PathVariable long idItem){
		ItemLista item = itemDao.buscarItem(idItem);
		return item;
	}
	
}
