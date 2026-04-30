package cgb.transfer.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Classe permettant le mapping d'un utilisateur entre la DB et l'API.
 */
@Entity
public class UserCGB {

	/**
	 * L'identifiant unique de l'utilisateur.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Le nom de l'utilisateur.
	 */
	private String username;

	/**
	 * Le mot de passe de l'utilisateur;
	 */
	private String password;

	/**
	 * L'email de l'utilisateur.
	 */
	private String email;

	/**
	 * Lien du client auquel appartient le compte.
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "customer_id")
	private Customer customer;

	/**
	 * Le rôle de l'utilisateur.
	 */
	private Role role;

	/**
	 * Getter de l'identifiant de l'utilisateur.
	 * 
	 * @return L'identifiant de l'utilisateur.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter de l'identifiant de l'utilisateur.
	 * 
	 * @param id Le nouvel identifiant de l'utilisateur.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter du nom de l'utilisateur.
	 * 
	 * @return Le nom de l'tuilisateur.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Setter du nom de l'utilisateur.
	 * 
	 * @param username Le nouveau nom de l'utilisateur.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Getter du mot de passe de l'utilisateur.
	 * 
	 * @return Le hash du mot de passe de l'utilisateur par bcrypt.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter du mot de passe de l'utilisateur. Hashage du mot de passe par bcrypt
	 * pour le stockage en base.
	 * 
	 * @param password Le nouveau mot de passe de l'utilisateur.
	 */
	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	/**
	 * Getter de l'adresse mail de l'utilisateur.
	 * 
	 * @return L'adresse mail de l'utilisateur.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Setter de l'adresse mail de l'utilisateur.
	 * 
	 * @param email La nouvelle adresse mail de l'utilisateur.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Getter du client associé à l'utilisateur.
	 * 
	 * @return Le client associé à l'utilisateur.
	 */
	public Customer getLinkedCustomer() {
		return customer;
	}

	/**
	 * Setter du client associé à l'utilisateur.
	 * 
	 * @param linkedCustomer Le nouveau client associé à l'utilisateur.
	 */
	public void setLinkedCustomer(Customer linkedCustomer) {
		this.customer = linkedCustomer;
	}

	/**
	 * Getter du rôle de l'utilisateur.
	 * 
	 * @return Le rôle de l'utilisateur.
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Setter du rôle de l'utilisateur.
	 * 
	 * @param role Le nouveau rôle de l'utilisateur.
	 */
	public void setRole(Role role) {
		this.role = role;
	}

}
