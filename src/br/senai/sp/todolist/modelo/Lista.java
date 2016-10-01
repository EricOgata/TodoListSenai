package br.senai.sp.todolist.modelo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Lista {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(length=100)
	private String titulo;
	/**
	 * FetchType Eager X Lazy: 
	 * Lazy: busca apenas as informações da tabela principal, para melhor desenpenho da busca. -Registros/+Desempenho;
	 * Eager: busca as informaçõs das tabelas associadas à principal, trazendo todos os registros. + Registros / - Desempenho;
	 * http://blog.caelum.com.br/entendendo-o-lazy-e-o-eager-load-da-jpa/	 * 
	 */
	@OneToMany(mappedBy="lista",cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	private List<ItemLista> itens;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public List<ItemLista> getItens() {
		return itens;
	}
	public void setItens(List<ItemLista> itens) {
		this.itens = itens;
	}
	
	@JsonProperty("feito") // informa o nome do parâmetro que será retornado
	public boolean isRealizada(){
		for(ItemLista item : itens){
			if(!item.isFeito()){
				return false;
			}
		}
		return true;	
	}
}
