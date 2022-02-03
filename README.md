# PACE: Programa Automatizado de Controle de Escala.
> 🌎 _(ASCP: Automated Scale Control Program)_

> ✔️ Status: version 1.0 finished

> ⚠️ I worked on the backend development only.

## 📋 Business rules

The program was designed to control the distribution of personnel (attorneys or agents) in the hearings in an automated and equitable manner, in addition to:
1. allow the registration of the list of audiences, and group them by their characteristics.
2. enable CRUD for lawyers, agents and attorneys.
3. prevent employees from working harder than others.

**Some fields in main Model in the _Pauta de Audiência_ Class:**
* processo
* nome do advogado
* objeto
* tipo (enum)
* procurador (many to one)
* mutirao (many to one)
* more 8 fields (...)

## 👨‍💻 Technologies used

   Backend    |        Frontend        |  Database | Arquitecture |     Hosting      |       IDE       |
:------------:|:----------------------:|:---------:|:------------:|:----------------:|:---------------:|
Java - Spring | Javascript - Bootstrap | Postgress |     Rest     | Heroku & Netlify | Eclipse & STS 4 |

