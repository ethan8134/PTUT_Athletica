export class Sessions {
  constructor(id, nom, date) {
    this._id = id;
    this._nom = nom;
    this._date = date;
  }

  get id() {
    return this._id;
  }

  get nom() {
    return this._nom;
  }

  get date() {
    return this._date;
  }

  toString() {
    return `--> ${this._nom} `;
  }
}
