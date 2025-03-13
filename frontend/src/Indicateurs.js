export class Indicateur {
  constructor(id, nom, valeur, unite, categorie) {
    this._id = id;
    this.nom = nom;
    this._valeur = valeur;
    this._unite = unite;
    this._categorie = categorie;
  }

  get id() {
    return this._id;
  }

  get nom() {
    return this.nom;
  }

  get valeur() {
    return this._valeur;
  }

  get unite() {
    return this._unite;
  }

  get categorie() {
    return this._categorie;
  }

  toString() {
    return `--> ${this.nom} `;
  }
}
