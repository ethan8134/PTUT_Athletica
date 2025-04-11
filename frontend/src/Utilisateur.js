export class Utilisateur {
  constructor(idPersonne, pseudo, email, mdp, preferences) {
    this._idPersonne = idPersonne;
    this._pseudo = pseudo;
    this._email = email;
    this._mdp = mdp;
    this._preferences = preferences;
  }

  get idPersonne() {
    return this._idPersonne;
  }

  get pseudo() {
    return this._pseudo;
  }

  get email() {
    return this._email;
  }

  get mdp() {
    return this._mdp;
  }

  get preferences() {
    return this._preferences;
  }

  toString() {
    return `--> ${this._pseudo} `;
  }
}

export default Utilisateur;
