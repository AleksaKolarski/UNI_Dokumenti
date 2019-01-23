function translation_init() {
  var searchParams;
  searchParams = new URLSearchParams(window.location.search);
  var searchLanguage = searchParams.get('lang');
  if (searchLanguage != 'rs' && searchLanguage != 'en') {
    searchLanguage = localStorage.getItem('lang');
    if (searchLanguage != 'rs' && searchLanguage != 'en') {
      searchLanguage = 'rs';
      localStorage.setItem('lang', searchLanguage);
    }
    searchParams.set('lang', searchLanguage);
    window.location.search = '?' + searchParams.toString();
  }
  else {
    if (localStorage.getItem('lang') == null || localStorage.getItem('lang') != searchLanguage) {
      localStorage.setItem('lang', searchLanguage)
    }
  }
}

function translation_init_param(initLanguage){
  var searchParams = new URLSearchParams(window.location.search);
  var searchLanguage = searchParams.get('lang');
  if(localStorage.getItem('lang') != initLanguage){
    localStorage.setItem('lang', initLanguage);
  }
  if(searchLanguage != initLanguage){
    searchParams.set('lang', initLanguage);
    window.location.search = '?' + searchParams.toString();
  }
}

translation_init();

var translationJSON = [
  {
    "source": "This service stores and enables the search of electronic documents.",
    "target_rs": "Ovaj servis skladisti i omogucava pretragu elektronskih dokumenata."
  },
  {
    "source": "Categories",
    "target_rs": "Kategorije"
  },
  {
    "source": "Books",
    "target_rs": "Knjige"
  },
  {
    "source": "Search",
    "target_rs": "Pretraga"
  },
  {
    "source": "Register",
    "target_rs": "Registracija"
  },
  {
    "source": "Username:",
    "target_rs": "Korisnicko ime:"
  },
  {
    "source": "Password:",
    "target_rs": "Lozinka:"
  },
  {
    "source": "Logged in user:",
    "target_rs": "Ulogovani korisnik:"
  },
  {
    "source": "Cancel",
    "target_rs": "Otkazi"
  },
  {
    "source": "Edit book",
    "target_rs": "Izmeni knjigu"
  },
  {
    "source": "Title",
    "target_rs": "Naslov"
  },
  {
    "source": "Author",
    "target_rs": "Autor"
  },
  {
    "source": "Keywords",
    "target_rs": "Kljucne reci"
  },
  {
    "source": "Publication year",
    "target_rs": "Godina objavljivanja"
  },
  {
    "source": "Language",
    "target_rs": "Jezik"
  },
  {
    "source": "Category",
    "target_rs": "Kategorija"
  },
  {
    "source": "Users",
    "target_rs": "Korisnici"
  },
  {
    "source": "Edit profile",
    "target_rs": "Izmeni profil"
  },
  {
    "source": "Edit password",
    "target_rs": "Izmeni sifru"
  },
  {
    "source": "Logout",
    "target_rs": "Izloguj se"
  },
  {
    "source": "Delete profile",
    "target_rs": "Obrisi profil"
  },
  {
    "source": "Login",
    "target_rs": "Uloguj se"
  },
  {
    "source": "Choose file",
    "target_rs": "Odaberi fajl"
  },
  {
    "source": "Upload",
    "target_rs": "Posalji"
  },
  {
    "source": "Confirm",
    "target_rs": "Potvrdi"
  },
  {
    "source": "Error while getting all languages",
    "target_rs": "Greska pri dobavljanju svih jezika"
  },
  {
    "source": "Error while getting all categories",
    "target_rs": "Greska pri dobavljanju svih kategorija"
  },
  {
    "source": "Error while uploading document",
    "target_rs": "Greska pri slanju dokumenta"
  },
  {
    "source": "Error while creating document",
    "target_rs": "Greska pri kreiranju dokumenta"
  },
  {
    "source": "Publication year not valid",
    "target_rs": "Godina izdavanja nije odgovarajuca"
  },
  {
    "source": "Keyword length must be less than 120",
    "target_rs": "Kljucne reci moraju biti krace od 120 karaktera"
  },
  {
    "source": "Author length not valid (5-120)",
    "target_rs": "Autor nije odgovarajuce duzine (5-120)"
  },
  {
    "source": "Title length not valid (5-80)",
    "target_rs": "Naslov nije odgovarajuce duzine (5-80)"
  },
  {
    "source": "Error while getting document information",
    "target_rs": "'Greska pri dobavljanju informacija o dokumentu'"
  },
  {
    "source": "Error while deleting document",
    "target_rs": "Greska pri brisanju dokumenta"
  },
  {
    "source": "Upload new book",
    "target_rs": "Kreiraj novu knjigu"
  },
  {
    "source": "Delete book",
    "target_rs": "Obrisi knjigu"
  },
  {
    "source": "Document name",
    "target_rs": "Ime dokumenta"
  },
  {
    "source": "Filename",
    "target_rs": "Naziv fajla"
  },
  {
    "source": "Uploader",
    "target_rs": "Kreator"
  },
  {
    "source": "Download",
    "target_rs": "Skini"
  },
  {
    "source": "Add new book",
    "target_rs": "Dodaj novu knjigu"
  },
  {
    "source": "Register first",
    "target_rs": "Registruj se"
  },
  {
    "source": "Edit",
    "target_rs": "Izmeni"
  },
  {
    "source": "Delete",
    "target_rs": "Obrisi"
  },
  {
    "source": "Sort",
    "target_rs": "Sortiraj"
  },
  {
    "source": "Create new category",
    "target_rs": "Kreiraj novu kategoriju"
  },
  {
    "source": "Name",
    "target_rs": "Naziv"
  },
  {
    "source": "Error while creating category",
    "target_rs": "Greska pri kreiranju kategorije"
  },
  {
    "source": "Category name length not valid (5-30)",
    "target_rs": "Naziv kategorije nije odgovarajuce duzine (5-30)"
  },
  {
    "source": "Edit category",
    "target_rs": "Izmeni kategoriju"
  },
  {
    "source": "Error while getting category info",
    "target_rs": "Greska pri dobavljanju informacija o kategoriji"
  },
  {
    "source": "Delete category",
    "target_rs": "Obrisi kategoriju"
  },
  {
    "source": "Error while deleting category",
    "target_rs": "Greska pri brisanju kategorije"
  },
  {
    "source": "Add new category",
    "target_rs": "Dodaj novu kategoriju"
  },
  {
    "source": "Wrong credentials",
    "target_rs": "Pogresni podaci"
  },
  {
    "source": "Keyword",
    "target_rs": "Kljucna rec"
  },
  {
    "source": "Content",
    "target_rs": "Sadrzaj"
  },
  {
    "source": "Register to download",
    "target_rs": "Registruj se da bi skinuo"
  },
  {
    "source": "Change password",
    "target_rs": "Izmena sifre"
  },
  {
    "source": "Repeat password:",
    "target_rs": "Ponovi sifru:"
  },
  {
    "source": "Error while changing password",
    "target_rs": "Greska pri promeni sifre"
  },
  {
    "source": "Both passwords must be same and have valid length (5-20)",
    "target_rs": "Obe lozinke se moraju poklapati i biti odgovarajuce duzine (5 - 20)"
  },
  {
    "source": "Firstname:",
    "target_rs": "Ime:"
  },
  {
    "source": "Lastname:",
    "target_rs": "Prezime:"
  },
  {
    "source": "Admin:",
    "target_rs": "Administrator"
  },
  {
    "source": "User already exists",
    "target_rs": "Korisnik vec postoji"
  },
  {
    "source": "Error while editing user",
    "target_rs": "Greska pri izmeni korisnika"
  },
  {
    "source": "Username length is not valid (5-10)",
    "target_rs": "Korisnicko ime nije odgovarajuce duzine (5-10)"
  },
  {
    "source": "Lastname length is not valid (5-30)",
    "target_rs": "Prezime nije odgovarajuce duzine (5-30)"
  },
  {
    "source": "Firstname length is not valid (5-30)",
    "target_rs": "Ime nije odgovarajuce duzine (5-30)"
  },
  {
    "source": "Delete user",
    "target_rs": "Brisanje korisnika"
  },
  {
    "source": "Error while deleting user",
    "target_rs": "Greska pri brisanju korisnika"
  },
  {
    "source": "Error while registering user",
    "target_rs": "Greska pri registraciji korisnika"
  },
  {
    "source": "Edit user",
    "target_rs": "Izmena korisnika"
  },
  {
    "source": "Add new",
    "target_rs": "Dodaj novog"
  },
  {
    "source": "Firstname",
    "target_rs": "Ime"
  },
  {
    "source": "Lastname",
    "target_rs": "Prezime"
  },
  {
    "source": "Username",
    "target_rs": "Korisnicko ime"
  },
  {
    "source": "Admin",
    "target_rs": "Administrator"
  },
  {
    "source": "Password",
    "target_rs": "Lozinka"
  },
  {
    "source": "AND",
    "target_rs": "I"
  },
  {
    "source": "OR",
    "target_rs": "ILI"
  },
  {
    "source": "NOT",
    "target_rs": "NE"
  }
];

function translation(translationKey) {
  var language = new URLSearchParams(window.location.search).get('lang');
  if (language == 'en') {
    return translationKey;
  }
  for (var i = 0; i < translationJSON.length; i++) {
    if (translationJSON[i].source == translationKey) {
      return translationJSON[i]['target_' + language];
    }
  }
  return 'translation error';
}