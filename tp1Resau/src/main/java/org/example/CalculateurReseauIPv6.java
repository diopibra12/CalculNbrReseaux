package org.example;

import java.util.Scanner;
//Ibrahima Diop
//Seydi Ahmeth Ndiaye

public class CalculateurReseauIPv6 {

    // Fonction pour calculer le nombre d'adresses IPv6 utilisables dans ce réseau
    public static long nombreAdressesIPv6Utilisables(int masque) {
        return (long) Math.pow(2, 128 - masque) - 2;
    }

    // Fonction pour calculer l'adresse du masque de sous-réseaux IPv6
    public static String adresseMasqueSousReseauxIPv6(int masque) {
        StringBuilder masqueIPv6 = new StringBuilder();
        int partieMasque = masque / 16;
        int bitMasque = masque % 16;

        for (int i = 0; i < 8; i++) {
            if (i < partieMasque) {
                masqueIPv6.append("ffff:");
            } else if (i == partieMasque) {
                masqueIPv6.append(String.format("%x", (1 << (16 - bitMasque)) - 1));
                masqueIPv6.append(":");
            } else {
                masqueIPv6.append("0:");
            }
        }

        return masqueIPv6.substring(0, masqueIPv6.length() - 1);
    }

    // Fonction pour calculer l'adresse du réseau IPv6
    public static String adresseReseauIPv6(String adresseIP, int masque) {
        String[] parties = adresseIP.split(":");
        int[] ip = new int[8];

        for (int i = 0; i < 8; i++) {
            ip[i] = Integer.parseInt(parties[i], 16);
        }

        int partieMasque = masque / 16;
        int bitMasque = masque % 16;

        for (int i = 0; i < 8; i++) {
            if (i < partieMasque) {
                ip[i] = 0;
            } else if (i == partieMasque) {
                ip[i] &= 0xFFFF << (16 - bitMasque);
            } else {
                
            }
        }

        StringBuilder adresseIPv6 = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            adresseIPv6.append(String.format("%04X", ip[i]));
            if (i < 7) {
                adresseIPv6.append(":");
            }
        }

        return adresseIPv6.toString();
    }

    // Fonction pour calculer l'adresse de la première adresse disponible sur ce réseau IPv6
    public static String premiereAdresseDisponibleIPv6(String adresseReseau) {
        // À adapter pour traiter les adresses IPv6
        String[] parties = adresseReseau.split(":");
        int[] ip = new int[8];

        for (int i = 0; i < 8; i++) {
            ip[i] = Integer.parseInt(parties[i], 16);
        }

        // Incrémenter la dernière partie pour obtenir la première adresse disponible
        ip[7]++;

        StringBuilder premiereAdresse = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            premiereAdresse.append(String.format("%04X", ip[i]));
            if (i < 7) {
                premiereAdresse.append(":");
            }
        }

        return premiereAdresse.toString();
    }

    // Fonction pour calculer l'adresse de la dernière adresse disponible sur ce réseau IPv6
    public static String derniereAdresseDisponibleIPv6(String adresseReseau, int masque) {
        
        String[] parties = adresseReseau.split(":");
        int[] ip = new int[8];

        for (int i = 0; i < 8; i++) {
            ip[i] = Integer.parseInt(parties[i], 16);
        }

        // Calculer la dernière adresse possible en fonction du masque
        int partieMasque = masque / 16;
        int bitMasque = masque % 16;

        for (int i = 7; i > partieMasque; i--) {
            if (i == partieMasque + 1) {
                ip[i] -= (1 << (16 - bitMasque)) - 1;
            } else {
                ip[i] = 0xFFFF;
            }
        }

        StringBuilder derniereAdresse = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            derniereAdresse.append(String.format("%04X", ip[i]));
            if (i < 7) {
                derniereAdresse.append(":");
            }
        }

        return derniereAdresse.toString();
    }

    // Fonction pour calculer l'adresse de diffusion IPv6
    public static String adresseDiffusionIPv6(String adresseReseau, int masque) {
        
        String[] parties = adresseReseau.split(":");
        int[] ip = new int[8];

        for (int i = 0; i < 8; i++) {
            ip[i] = Integer.parseInt(parties[i], 16);
        }

        // Calculer l'adresse de diffusion en fonction du masque
        int partieMasque = masque / 16;
        int bitMasque = masque % 16;

        for (int i = 7; i > partieMasque; i--) {
            if (i == partieMasque + 1) {
                ip[i] |= (1 << (16 - bitMasque)) - 1;
            } else {
                ip[i] = 0xFFFF;
            }
        }

        StringBuilder adresseDiffusion = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            adresseDiffusion.append(String.format("%04X", ip[i]));
            if (i < 7) {
                adresseDiffusion.append(":");
            }
        }

        return adresseDiffusion.toString();
    }

    // Fonction pour vérifier le type d'adresse IPv6
    public static String typeAdresseIPv6(String adresseIP) {
        
        // Exemple simplifié : si l'adresse commence par "fe80:", c'est une adresse link-local
        if (adresseIP.startsWith("fe80:")) {
            return "Link-Local";
        } else {
            return "Non défini";
        }
    }


    

    // Exemple d'utilisation pour IPv6
    public static void ipv6_menu() {
        Scanner scanner = new Scanner(System.in);
        String adresseIPv6test = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        int masqueIPv6test = 64;
        System.out.print("Veuillez saisir l'adresse IPv6 (format xxx.xxx.xxx.xxx) : ");
        String adresseIPv6 = scanner.next();
        System.out.print("Veuillez saisir le masque (format /xx) : ");
        int masqueIPv6 = scanner.nextInt();
        System.out.println("Nombre d'adresses IPv6 utilisables : " + nombreAdressesIPv6Utilisables(masqueIPv6));
        System.out.println("Adresse du masque de sous-réseaux IPv6 : " + adresseMasqueSousReseauxIPv6(masqueIPv6));
        System.out.println("Adresse du réseau IPv6 : " + adresseReseauIPv6(adresseIPv6, masqueIPv6));
        System.out.println("Première adresse disponible IPv6 : " + premiereAdresseDisponibleIPv6(adresseReseauIPv6(adresseIPv6,masqueIPv6)));
        System.out.println("Dernière adresse disponible IPv6 : " + derniereAdresseDisponibleIPv6(adresseReseauIPv6(adresseIPv6, masqueIPv6),masqueIPv6));
        System.out.println("Adresse de diffusion IPv6 : " + adresseDiffusionIPv6(adresseIPv6, masqueIPv6));
        System.out.println("Type d'adresse IPv6 : " + typeAdresseIPv6(adresseIPv6));
    }
}
