# NSOC-Projet
NSOC Projet : Alfred - Majordome de Batman

<h2>Installer git</h2>

    - Télécharger git : https://git-scm.com/downloads
    - Lors de l'installation, cocher la case permettant d'utiliser les commandes git sur le terminal classique
    - Lancer son terminal
    - Taper votre nom pour vos futurs commits : git config --global user.name "votre_nom_ici"
    - Taper l'adresse email que vous utilisez sur github : git config --global user.email "votre_email@votre_email.com"
    
<h2>Créer une clé SSH et l'ajouter sur son compte Github</h2>
    - Créer une nouvelle clé ssh : ssh-keygen -t rsa -b 4096 -C "adresse_email_utilisée_sur_github"
    - Entrer le chemin où sera enregistrée la clé SSH ainsi que son nom
        exemple : /documents/NSOC/clé_ssh_github
        Généralement sur Linux, l'enregistrer dans ~/.ssh/clé_ssh_github
    - Ne pas mettre de passphrase (press enter)
    - Lancer le ssh-agent : 
        Si vous utilisez git bash : eval $(ssh-agent -s),
        Si vous utilisez votre terminal classique : eval "$(ssh-agent -s)"
    - Ajouter votre clé ssh au ssh-agent : ssh-add ~/chemin_vers_votre_clé_ssh/nom_de_la_clé
        exemple: ssh-add ~/documents/NSOC/clé_ssh_github
    - Afficher la clé ssh publique : cat ~/chemin_vers_votre_clé_ssh.pub
        exemple : cat ~/documents/NSOC/clé_ssh_github.pub
    - Copier TOUTE la clé (de "ssh-rsa ..." à la fin de votre adresse email)
    - Aller sur Github
    - Cliquer sur votre avatar en haut à droite, puis cliquer sur "Settings"
    - Aller dans "SSH and GPG keys"
    - Dans l'espace "SSH keys", cliquer sur "New SSH key"
    - Mettre un titre à votre clé SSH github
        exemple : "clé ssh windows asus"
    - Copier la clé publique dans l'espace "Key"
    - Cliquer sur "Add SSH key"

<h2>Récupérer le dépôt</h2>

    - Se placer à l'endroit où vous voulez copier le projet
    - git clone "git@github.com:Esir-TICB-2017/NSOC-Projet.git"
    
<h2>Obtenir une aide pour les commandes git</h2>

    - git help

## Import the project into Capella

After cloning the repo, simply import the whole folder as a capella project.
