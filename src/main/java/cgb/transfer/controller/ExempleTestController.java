package cgb.transfer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Classe de test du bon fonctionnement de l'api.
 */
@RestController
@RequestMapping("/test")
public class ExempleTestController {
	
	@GetMapping("/{id}")
	/**
	 * Permet de tester la récupération d'une tâche dans l'api de test.
	 * 
	 * @param id  L'identifiant passé dans la route d'URI.
	 * @return  Un message de recu.
	 */
	public String obtenirTache(@PathVariable int id) {
		
		return "Recu : " + id ; // Si application properties avec spring.thymeleaf.prefix=classpath:/vues/

	}
	
	@GetMapping("/")
	/**
	 * Route de test générale.
	 * 
	 * @return  Un message si tout fonctionne.
	 */
	public String testVide() {
		
		return "Racine sous test "  ; // Si application properties avec spring.thymeleaf.prefix=classpath:/vues/

	}
}
