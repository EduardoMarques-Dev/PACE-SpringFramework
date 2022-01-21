import baseURL from '../../ambiente/baseURL.js'

var url = baseURL + 'pautas/';
var pautas;
var pauta;
var quantidadePautistas;
var salaAtual;
var mensagem = '';

var pautaJson = {
  "data": "",
  "hora": "",
  "turno": "",
  "sala": "",
  "processo": "",
  "nomeParte": "",
  "cpf": "",
  "nomeAdvogado": "",
  "objeto": "",
  "vara": "",
  "tipo": "",
  "procurador": {}
}

///////////EXIBIÇÃO ///////////

window.onload = function () {
  axios.get(url).then(response => {
    pautas = response.data;
    pautas.forEach(listar);
  }).catch(error => console.error(error));

  axios.get(baseURL + "procuradores").then(response => {
    var listaDePautistas = response.data;
    quantidadePautistas = listaDePautistas.length;
    // console.log(quantidadePautistas);
  }).catch(error => console.error(error));

};

function listar(pauta) {
  pauta.data = formatarData(pauta.data);
  var tabela = $('#dataTable').DataTable();
  tabela.row.add([
    pauta.data,
    pauta.hora,
    pauta.sala,
    pauta.processo,
    pauta.nomeParte,
    pauta.cpf,
    pauta.objeto,
    pauta.nomeAdvogado
  ]).draw(false);
}

//////////// CRUD /////////////

function cadastrar(pautaJson) {
  axios.post(url, pautaJson).then(response => {
    if (response.data !== "") {
      listar(response.data);
      limparCampos();
    }
  }).catch(error => console.error(error));
}

function deletar(id) {
  var table = $('#dataTable').DataTable();
  axios.delete(url + id).then(response => {
    console.log("Status ", response.status);
    table.row('.selected').remove().draw(false);
  }).catch(error => {
    console.error(error);
    location.reload();
  });
}
//////////// UTIL /////////////

function pesquisar(pautaJson) {
  var tabela = $('#dataTable').DataTable();
  var pautaDaPesquisa;
  console.log(pautaJson)
  axios.get(url).then(response => {
    var pautas = response.data;
    pautaDaPesquisa = pautas;

    if (pautaJson.data) {
      pautaDaPesquisa = pautaDaPesquisa.filter(item => item.data == pautaJson.data);
    }

    if (pautaJson.hora) {
      pautaDaPesquisa = pautaDaPesquisa.filter(item => item.hora == pautaJson.hora);
    }

    if (pautaJson.sala) {
      pautaDaPesquisa = pautaDaPesquisa.filter(item => item.sala == pautaJson.sala);
    }

    if (pautaJson.processo) {
      pautaDaPesquisa = pautaDaPesquisa.filter(item => item.processo == pautaJson.processo);
    }

    if (pautaJson.nomeParte) {
      console.log(pautaJson.nomeParte)
      pautaDaPesquisa = pautaDaPesquisa.filter(item => item.nomeParte == pautaJson.nomeParte);
      console.log(pautaDaPesquisa)
    }

    if (pautaJson.cpf) {
      pautaDaPesquisa = pautaDaPesquisa.filter(item => item.cpf == pautaJson.cpf);
    }

    if (pautaJson.objeto) {
      pautaDaPesquisa = pautaDaPesquisa.filter(item => item.objeto == pautaJson.objeto);
    }

    if (pautaJson.nomeAdvogado) {
      pautaDaPesquisa = pautaDaPesquisa.filter(item => item.nomeAdvogado == pautaJson.nomeAdvogado);
    }

    if (pautaJson.vara) {
      pautaDaPesquisa = pautaDaPesquisa.filter(item => item.vara == pautaJson.vara);
    }

    if (pautaJson.tipo) {
      pautaDaPesquisa = pautaDaPesquisa.filter(item => item.tipo == pautaJson.tipo);
    }

    if (pautaDaPesquisa) {

      tabela.rows().remove().draw();

      pautaDaPesquisa.forEach(listar);
    } else {
      tabela.rows().remove().draw();
      pautas.forEach(listar);
    }
    console.log("Status " + response.status);

  }).catch(error => console.error(error));
}

function formatarData(localDate) {
  if (localDate !== null && localDate.length == 10) {
    if (localDate.indexOf("-") == 4 && localDate.length == 10) {
      localDate = localDate.substring(8, 10) + "-" + localDate.substring(5, 7) + "-" + localDate.substring(0, 4);
    } else {
      if (localDate.indexOf("/") == 2 || localDate.indexOf("-") == 2) {
        if (localDate.substring(0, 2) > 31 || localDate.substring(3, 5) > 12) {
          localDate = "null";
        } else {
          localDate = localDate.substring(6, 10) + "-" + localDate.substring(3, 5) + "-" + localDate.substring(0, 2);
        }
      }
    }
  }
  return localDate;
}

function removerEspacos(string) {
  return string.replace(/^\s+|\s+$/g, "");
}

function formatar(key, textarea) {
  textarea = textarea.split(/\r|\n/);
  for (var i in textarea) {
    if (textarea[i] == '') {
      if (key == 'processo' || key == 'data' || key == 'sala' || key == 'hora' || key == 'turno')
        campoNulo(key);
    }
    textarea[i] = removerEspacos(textarea[i]);
    if (key == 'data') {
      if (textarea[i].length == 10) {
        if (textarea[i].indexOf("/") == 2 || textarea[i].indexOf("-") == 2) {
          if (textarea[i].substring(0, 2) > 31 || textarea[i].substring(3, 5) > 12) {
            textarea[i] = "null";
          } else {
            textarea[i] = textarea[i].substring(6, 10) + "-" + textarea[i].substring(3, 5) + "-" + textarea[i].substring(0, 2);
          }
        } else {
          var linha = i;
        }
      } else {
        var linha = i + 1;
        mensagem = `Data dd/mm/aaaa com formato inválido: ${textarea[i]}`;
      }
    }
    else if (key == "hora") {
      let pattern = /^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/;
      if (!(pattern.test(textarea[i]))) {
        return;
      }
    }
  }
  return textarea;
}

function reset() {
  console.log("oi")
  var form = document.getElementById("form");
  var nome = form.nome.value;
  $(document).ready(function () {

    document.querySelector("data").reset();
  });
}

function validarPauta(pauta) {
  var pautaValida = pauta;
  console.log(pautaValida);

  return pautaValida;
}

//////////// REVISAO /////////////
function mask() {
  var SPMaskBehavior = function (val) {
    return val.replace(/\D/g, '')[0] === '2' ? 'AE:CD' : 'AB:CD';
  }
}
const dados = {
  "animais": [{
    "id": 1,
    "tipo": "Cachorro",
    "nome": "Max",
    "tags": "Border Collier"
  }, {
    "id": 2,
    "tipo": "Cachorro",
    "nome": "Fini",
    "tags": "Lhasa Apso"
  }]
};

var json = {
  "processo": "101"
}

var json2 = {
  "processo": "202"
}

var listaJson = [];

listaJson.push(json)
listaJson.push(json2)

//console.log(listaJson)

const ignorar = ['tipo'];

function copiarExcepto(original, excluirProps) {
  return Object.keys(original)
    .filter(prop => !excluirProps.includes(prop)) // excluir as chaves que não interessam
    .reduce((obj, prop) => {
      // adicionar as chaves relevantes ao novo objeto
      return {
        ...obj,
        [prop]: original[prop]
      }
    }, {});
}

const animais = dados.animais.reduce((obj, animal) => {
  const tipo = animal.tipo + 's'; // criar a chave por tipos
  const novoObjeto = copiarExcepto(animal, ignorar); // copiar o objeto sem as chaves que não queremos
  obj[tipo] = (obj[tipo] || []).concat(novoObjeto); // adicionar o animal à lista
  return obj;
}, {});

// console.log(JSON.stringify(animais, null, 2));

function addJson(pauta) {

  // var novaPauta = {"pauta": []};
  // novaPauta.pauta.push(pauta)
  console.log(pauta)

  pautaJson.data = pauta[0];
  pautaJson.hora = pauta[1];
  pautaJson.turno = pauta[2];
  pautaJson.sala = pauta[3];
  pautaJson.processo = pauta[4];
  pautaJson.nomeParte = pauta[5];
  pautaJson.cpf = pauta[6];
  pautaJson.nomeAdvogado = pauta[7];
  pautaJson.objeto = pauta[8];
  pautaJson.vara = pauta[9];
  pautaJson.tipo = pauta[10];

  cadastrar(pautaJson);

  if (pauta[3]) {
    axios.get(url).then(response => {
      var pautasAtuais = response.data;
      var pautaExiste = pautasAtuais.filter(item => item.processo == pauta[3]);
      pautaExiste = pautaExiste[0];
      if (pautaExiste)
        console.log("Já existe pauta com esse processo: " + pautaExiste.processo);
      else {
        console.log("Status " + response.status)

        pautaJson.data = pauta[0];
        pautaJson.hora = pauta[1];
        pautaJson.turno = pauta[2];
        pautaJson.sala = pauta[3];
        pautaJson.processo = pauta[4];
        pautaJson.nomeParte = pauta[5];
        pautaJson.cpf = pauta[6];
        pautaJson.nomeAdvogado = pauta[7];
        pautaJson.objeto = pauta[8];
        pautaJson.vara = pauta[9];
        pautaJson.tipo = pauta[10];
        //cadastrar(pautaJson);
      }
    }).catch(error => console.error(error));
  }
}

//////////// BOTÕES /////////////

$('#cadastrar-pauta').on('click', function () {
  var data = formatar("data", document.querySelector('#data-pauta').value);
  var hora = formatar("hora", document.querySelector('#hora-pauta').value);
  if (typeof hora === "undefined") {
    alert("Algum valor de hora está no formato incorreto. Por favor, verifique se todas se encontram no formato HH:MM.");
    return;
  }
  var turno = formatar("turno", document.querySelector('#turno-pauta').value.toUpperCase());
  var sala = formatar("sala", document.querySelector('#sala-pauta').value);
  var processo = formatar("processo", document.querySelector('#processo').value);
  var nomeParte = formatar("nomeParte", document.querySelector('#nome-parte').value);
  var cpf = formatar("cpf", document.querySelector('#cpf-pauta').value);
  var nomeAdvogado = formatar("nomeAdvogado", document.querySelector('#nome-advogado').value);
  var objeto = formatar("objeto", document.querySelector('#objeto').value);

  var vara = document.getElementById('vara');
  vara = vara.options[vara.selectedIndex].value;

  var tipo = document.getElementById('tipo');
  tipo = tipo.options[tipo.selectedIndex].value.toUpperCase();

  var listaDePautas = [];
  var i = 0;
  do {
    if (processo[i].trim() !== "") {

      pautaJson.data = data[i];
      pautaJson.hora = hora[i];
      pautaJson.turno = turno[i];
      pautaJson.sala = sala[i];
      pautaJson.processo = processo[i];
      pautaJson.nomeParte = nomeParte[i];
      pautaJson.cpf = cpf[i];
      pautaJson.nomeAdvogado = nomeAdvogado[i];
      pautaJson.objeto = objeto[i];
      pautaJson.vara = vara;
      pautaJson.tipo = tipo;

      for (var key in pautaJson) {
        if (typeof pautaJson[key] == 'undefined')
          pautaJson[key] = "";

        //console.log(key)

      }

      var formattedJSON = JSON.stringify(pautaJson);
      listaDePautas.push(JSON.parse(formattedJSON));
      //console.log(listaDePautas)

    }

    i++;
  } while (i < processo.length);

  console.log(listaDePautas);
  //addJson(listaDePautas);

  axios.post(url, listaDePautas).then(response => {
    pautas = response.data;
    listaDePautas.forEach(listar);
    var status = response.status;
    console.log("Status " + response.status);
    if (response.status == 200) {
      alertar('Pautas foram cadastradas.');
    }
    // location.reload();
    limparCampos();
  }).catch((error) => {
    // Trate o erro aqui.
    console.log('Houve um erro. ', error.message || error)
    alertar('Erro ao cadastrar. ' + mensagem + '\nMensagem: ' + error.message || error);

  });

});

function alertar(aviso) {
  alert(aviso);
}

function campoNulo(campo) {
  alert('Aviso: ' + campo + ' é obrigatório.');
}

function mascara(key, valor) {
  // jQuery(function($){
  //   $("#data-pauta").mask("99/99/9999");
  //   $("#campoTelefone").mask("(999) 999-9999");
  //   $("#campoSenha").mask("***-****");
  //   });
}

$('#excluir').click(function () {
  var table = $('#dataTable').DataTable();
  var pauta = table.row('.selected').data();
  var processo = pauta[3];
  pautaJson = pautas.filter(item => item.processo == processo);

  var id = pautaJson[0].id;// primeira pauta com o processo pesquisado

  deletar(id);
});

$('#editar').click(function () {
  var table = $('#dataTable').DataTable();
  pauta = table.row('.selected').data();
  var processo = pauta[3];

  sessionStorage.setItem('processo', processo);
});

function limparCampos() {
  document.getElementById("data-pauta").value = "";
  document.getElementById("hora-pauta").value = "";
  document.getElementById("turno-pauta").value = "";
  document.getElementById("sala-pauta").value = "";
  document.getElementById("processo").value = "";
  document.getElementById("nome-parte").value = "";
  document.getElementById("cpf-pauta").value = "";
  document.getElementById("nome-advogado").value = "";
  document.getElementById("objeto").value = "";
  document.getElementById("vara").value = "1ª Vara Federal";
  document.getElementById("tipo").value = "Conciliação";
}

$('#pesquisar').click(function () {

  pautaJson.data = document.querySelector('#data-pauta').value.trim();
  pautaJson.hora = document.querySelector('#hora-pauta').value.trim();
  pautaJson.nomeAdvogado = document.querySelector('#nome-advogado').value.trim();
  pautaJson.nomeParte = document.querySelector('#nome-parte').value.trim();
  pautaJson.objeto = document.querySelector('#objeto').value.trim();
  pautaJson.processo = document.querySelector('#processo').value.trim();
  pautaJson.sala = document.querySelector('#sala-pauta').value.trim();
  pautaJson.cpf = document.querySelector('#cpf-pauta').value.trim();

  pautaJson.vara = document.getElementById('vara').value;

  pautaJson.tipo = document.getElementById('tipo').value;

  pesquisar(pautaJson);
});