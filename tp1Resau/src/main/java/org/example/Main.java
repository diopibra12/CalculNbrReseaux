package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//Ibrahima Diop
//Seydi Ahmeth Ndiaye
public class Main {


//Fonction pour convertir une adresse IP en tableau d'entiers
    private static int[] convertirAdresseIP(String adresseIP) {
        String[] parties = adresseIP.split("\\.");
        int[] adresse = new int[4];
        for (int i = 0; i < 4; i++) {
            adresse[i] = Integer.parseInt(parties[i]);
        }
        return adresse;
    }

    // Fonction pour convertir le masque en adresse IP
    private static int[] convertisseurMasqueEnIPAdress(int masque) {
        int[] masqueIP = new int[4];
        for (int i = 0; i < 4; i++) {
            int bit = Math.min(masque, 8);  // Nombre de bits à mettre à 1 dans cet octet
            masqueIP[i] = 256 - (1 << (8 - bit));
            masque -= bit;
        }
        return masqueIP;
    }


    // Fonction pour appliquer le masque à une adresse IP
    public static long nombreAdressesIPUtilisables(int masque) {
        return (long) Math.pow(2, 32 - masque) - 2;
    }

    // Fonction pour calculer l'adresse du masque de sous-réseaux (si applicable)
    public static String adresseMasqueSousReseaux(int masque) {
        int[] masqueIP = convertisseurMasqueEnIPAdress(masque);
        return String.format("%d.%d.%d.%d", masqueIP[0], masqueIP[1], masqueIP[2], masqueIP[3]);
    }

    // Fonction pour calculer l'adresse du réseau
    public static String adresseReseau(String adresseIP, int masque) {
        int[] ip = convertirAdresseIP(adresseIP);
        int[] masqueIP = convertisseurMasqueEnIPAdress(masque);

        // Appliquer le masque
        for (int i = 0; i < 4; i++) {
            ip[i] &= masqueIP[i];
        }

        return String.format("%d.%d.%d.%d", ip[0], ip[1], ip[2], ip[3]);
    }

    // Fonction pour calculer l'adresse de la première adresse disponible sur ce réseau
    public static String premiereAdresseDisponible(String adresseReseau) {
        int[] ip = convertirAdresseIP(adresseReseau);

        // Incrémenter la dernière partie de l'adresse
        ip[3]++;

        return String.format("%d.%d.%d.%d", ip[0], ip[1], ip[2], ip[3]);
    }

    // Fonction pour calculer l'adresse de la dernière adresse disponible sur ce réseau
    public static String derniereAdresseDisponible(String adresseReseau, int masque) {
        int[] ip = convertirAdresseIP(adresseReseau);
        int nombreAdresses = (int) nombreAdressesIPUtilisables(masque);

        // Ajouter le nombre d'adresses disponibles
        ip[3] += nombreAdresses;

        return String.format("%d.%d.%d.%d", ip[0], ip[1], ip[2], ip[3]);
    }

    // Fonction pour calculer l'adresse de diffusion
    public static String adresseDiffusion(String adresseReseau, int masque) {
        int[] ip = convertirAdresseIP(adresseReseau);
        int nombreAdresses = (int) nombreAdressesIPUtilisables(masque);

        // Ajouter le nombre d'adresses disponibles - 1
        ip[3] += nombreAdresses + 1;

        return String.format("%d.%d.%d.%d", ip[0], ip[1], ip[2], ip[3]);
    }

    // Fonction pour vérifier s'il s'agit d'une adresse routable (locale ou non), de bouclage ou link-local
    public static String typeAdresse(String adresseIP) {
        int[] ip = convertirAdresseIP(adresseIP);

        // Vérifier si l'adresse est dans la plage des adresses privées (locale)
        if ((ip[0] == 10) ||
                (ip[0] == 172 && ip[1] >= 16 && ip[1] <= 31) ||
                (ip[0] == 192 && ip[1] == 168)) {
            return "Adresse privée (locale)";
        }

        // Vérifier s'il s'agit de l'adresse de bouclage
        if (ip[0] == 127) {
            return "Adresse de bouclage";
        }

        // Vérifier s'il s'agit d'une adresse link-local
        if (ip[0] == 169 && ip[1] == 254) {
            return "Adresse link-local";
        }

        // Sinon, considérer comme une adresse routable
        return "Adresse routable";
    }
    // Fonction pour calculer le masque nécessaire en fonction du nombre d'hôtes
    public static int calculerMasqueIPv4(int nombreHotesRequis) {
        int bitsNecessaires = 0;
        while (Math.pow(2, bitsNecessaires) < nombreHotesRequis) {
            bitsNecessaires++;
        }
        return Math.max(0, 32 - bitsNecessaires);

    }

    // Fonction pour diviser un réseau IPv4 en sous-réseaux en fonction du nombre d'hôtes
    public static List<String> diviserReseauIPv4(String adresseIPv4, int masqueInitial, int nombreHotesRequis) {
        // Calculer le masque nécessaire en fonction du nombre d'hôtes requis
        int masqueNouveau = calculerMasqueIPv4(nombreHotesRequis);
        // Afficher le masque nécessaire
        System.out.println("Masque nécessaire pour " + nombreHotesRequis + " hôtes : /" + masqueNouveau);
        // Créer une liste pour stocker les adresses de sous-réseaux
        List<String> sousReseaux = new ArrayList<>();
        // Convertir l'adresse IPv4 en tableau d'entiers
        String[] parties = adresseIPv4.split("\\.");
        int[] ip = new int[4];
        for (int i = 0; i < 4; i++) {
            ip[i] = Integer.parseInt(parties[i]);
        }
        // Calculer le nombre de sous-réseaux
        int nombreSousReseaux = (int) Math.pow(2, masqueNouveau - masqueInitial);
        // Calculer la taille de chaque sous-réseau
        int tailleSousReseau = (int) Math.pow(2, 32 -  masqueNouveau);
        // Générer les sous-réseaux
        for (int i = 0; i < nombreSousReseaux; i++) {
            // Calculer la première adresse de chaque sous-réseau
            int premiereAdresse = ip[3] + i * tailleSousReseau;
            // Construire l'adresse IPv4 de la première adresse du sous-réseau
            String adressePremiere = String.format("%d.%d.%d.%d", ip[0], ip[1], ip[2], premiereAdresse);

            // Ajouter l'adresse du sous-réseau à la liste
            sousReseaux.add(adressePremiere);
        }

        return sousReseaux;
    }
    public static void afficherInformationsSousReseaux(List<String> sousReseaux, int masque) {
        for (String sousReseau : sousReseaux) {
            System.out.println("Adresse Sous-réseau : " + adresseReseau(sousReseau,masque));
            System.out.println("Première adresse : " + premiereAdresseDisponible(adresseReseau(sousReseau, masque)));
            System.out.println("Dernière adresse : " + derniereAdresseDisponible(adresseReseau(sousReseau, masque), masque));
            System.out.println("Adresse de broadcast : " + adresseDiffusion(adresseReseau(sousReseau, masque), masque));
            System.out.println();
        }
    }
public static void ipv4_menu(){
    Scanner scanner = new Scanner(System.in);
    System.out.print("Veuillez saisir l'adresse IPv4 (format xxx.xxx.xxx.xxx) : ");
    String adresseIP = scanner.next();
    System.out.print("Veuillez saisir le masque (format /xx) : ");
    int masque = scanner.nextInt();
    System.out.println("Nombre d'adresses IP utilisables : " + nombreAdressesIPUtilisables(masque));
    System.out.println("Adresse du masque de sous-réseaux : " + adresseMasqueSousReseaux(masque));
    System.out.println("Adresse du réseau : " + adresseReseau(adresseIP, masque));
    System.out.println("Première adresse disponible : " + premiereAdresseDisponible(adresseReseau(adresseIP, masque)));
    System.out.println("Dernière adresse disponible : " + derniereAdresseDisponible(adresseReseau(adresseIP, masque), masque));
    System.out.println("Adresse de diffusion : " + adresseDiffusion(adresseReseau(adresseIP, masque), masque));
    System.out.println("Type d'adresse : " + typeAdresse(adresseIP));
    System.out.println("Vous avez la possibilite de sous -resauter votre adresse ip ");
    System.out.print("Veuillez saisir le nombre d'hôtes requis par sous-reseau : ");
    int nombreHotesRequis = scanner.nextInt();
    // Appeler la fonction pour diviser le réseau IPv4 en sous-réseaux
    List<String> sousReseaux = diviserReseauIPv4( adresseReseau(adresseIP, masque), masque, nombreHotesRequis);
    // Afficher les sous-réseaux générés
    System.out.println("Sous-réseaux créés :");
    afficherInformationsSousReseaux(sousReseaux,calculerMasqueIPv4(nombreHotesRequis));
    }


    // Exemple d'utilisation
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choix=0 ;
        do {
            System.out.println("1.IPV4 ");
            System.out.println("2.IPV6 ");
            System.out.println("0.quitter");
            System.out.println("ENTRER VOTRE CHOIX ");
             choix = Integer.parseInt(scanner.next());
            switch (choix){
                case 1 :
                    ipv4_menu();
                    break;
                case 2 :
                    CalculateurReseauIPv6.ipv6_menu();
                    break;
            }
        }while(choix!=0);

    }
}