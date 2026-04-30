package cgb.transfer.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Classe permettant le mapping d'un client entre la DB et l'API.
 */
@Entity
public class Customer {

	/**
	 * L'identifiant du client.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Le nom du client.
	 */
	private String name;

	/**
	 * L'adresse du siège social du client.
	 */
	private String address;

	/**
	 * Le LEI (Legal Entity Identifier) du client.
	 */
	private String lei;

	/**
	 * Liste des comptes courants du client.
	 */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Account> myAccounts;

	/**
	 * Liste des comptes bénéficiaires du client.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	private Set<Account> recipientAccounts;

	/**
	 * Liste des utilisateurs associés au client.
	 */
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private Set<UserCGB> linkedUsers;

	/**
	 * Getter de l'identifiant du client.
	 * 
	 * @return L'identifiant du client.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter de l'identifiant du client.
	 * 
	 * @param id Le nouvel identifiant du client.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter du nom du client.
	 * 
	 * @return Le nom du client.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter du nom du client.
	 * 
	 * @param name Le nouveau nom du client.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter de l'adresse du client.
	 * 
	 * @return L'adresse du client.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Setter de l'adresse du client.
	 * 
	 * @param address La nouvelle adresse du client.
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Getter du LEI du client.
	 * 
	 * @return Le LEI du client.
	 */
	public String getLei() {
		return lei;
	}

	/**
	 * Setter du LEI du client.
	 * 
	 * @param lei Le nouveau LEI du client.
	 */
	public void setLei(String lei) {
		this.lei = lei;
	}

	/**
	 * Getter de la liste des comptes courants du client.
	 * 
	 * @return La liste des comptes courants du client.
	 */
	public Set<Account> getMyAccounts() {
		return myAccounts;
	}

	/**
	 * Setter de la liste des comptes courant du client.
	 * 
	 * @param myAccounts La nouvelle liste de comptes courants du client.
	 */
	public void setMyAccounts(Set<Account> myAccounts) {
		this.myAccounts = myAccounts;
	}

	/**
	 * Méthode d'ajout d'un compte à la liste des comptes courants du client.
	 * 
	 * @param account Le compte à ajouter à la liste.
	 */
	public void addToMyAccounts(Account account) {
		this.myAccounts.add(account);
	}

	/**
	 * Getter de la liste des comptes bénéficiaires du client.
	 * 
	 * @return La liste des comptes bénéficiaires du client.
	 */
	public Set<Account> getRecipientAccounts() {
		return recipientAccounts;
	}

	/**
	 * Setter de la liste des comptes bénéficiaires du client.
	 * 
	 * @param recipientAccounts La nouvelle liste des comptes bénéficiaires du
	 *                          client.
	 */
	public void setRecipientAccounts(Set<Account> recipientAccounts) {
		this.recipientAccounts = recipientAccounts;
	}

	/**
	 * Méthode d'ajout d'un compte à la liste des comptes bénéficiaires du client.
	 * 
	 * @param recipientAccount Le compte à ajouter à la liste.
	 */
	public void addToRecipientAccounts(Account recipientAccount) {
		this.recipientAccounts.add(recipientAccount);
	}

	/**
	 * Getter de la liste des utilisateurs associés à ce client.
	 * 
	 * @return La liste des utilisateurs associés à ce client.
	 */
	public Set<UserCGB> getLinkedUsers() {
		return linkedUsers;
	}

	/**
	 * Setter de la liste des utilisateurs associés à ce client.
	 * 
	 * @param linkedUsers La nouvelle liste des utilisateurs associés à ce client.
	 */
	public void setLinkedUsers(Set<UserCGB> linkedUsers) {
		this.linkedUsers = linkedUsers;
	}

	/**
	 * Méthode d'ajout d'un utilisateur à la liste des utilisateurs associés à ce
	 * client.
	 * 
	 * @param user L'utilisateur à ajouter.
	 */
	public void addUser(UserCGB user) {
		this.linkedUsers.add(user);
	}

}
