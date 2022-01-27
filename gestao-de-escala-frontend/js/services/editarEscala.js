import baseURL from '../../ambiente/baseURL.js'

var url = baseURL + 'escala/';

var escalas;

var pautas;
var processo;
var pautistaId;
var procuradores = [];
var pautistaAntigo;

var escalaJson = {
  "vara": ""//,
  // "dataInicial": "",
  // "dataFinal": ""
}

var pautaJson = {
  // "id": "",
   "data": "",
   "hora": "",
   "sala": "",
   "processo": "",
   "nomeParte": "",
   "cpf": "",
   "nomeAdvogado": "",
   "objeto": "",
   "vara" : "",
   "tipo": "",
   "pautista": {},
   "procuradorDto": {}
}

var pautistaJson = {
  "nome": "",
  "status": "",
  "dataInicial":"",
  "dataFinal":"",
  "grupo":"",
  "saldo": ""
}

window.onload = function() {

  
  axios.get(baseURL + 'pauta/').then(response => {


    pautas = response.data;
    processo = sessionStorage.getItem('processo'); 
    pautaJson = pautas.filter(item =>  item.processo == processo);
    pautaJson = pautaJson[0];
    
    var procurador = pautaJson.pautista;
    if(procurador)
       pautistaId = procurador.id;
    if(procurador == null){
      pautaJson.pautista = "";
    }else{
      pautaJson.pautista = procurador.nome;
    }

    pautistaAntigo = pautaJson.pautista;
    console.log(procurador.nome)
    console.log(procurador.id)

    document.getElementById("data-pauta").value = pautaJson.data;
    document.getElementById("hora-pauta").value = pautaJson.hora;
    document.getElementById("sala-pauta").value = pautaJson.sala;
    document.getElementById("processo").value = pautaJson.processo;
    document.getElementById("pautista").value = procurador.nome;

    axios.get(baseURL + 'pautista/disponiveis?data=' + pautaJson.data).then(response => {
      var lista = response.data;
      lista.forEach(function(procurador){
          procuradores.push(procurador);
      });
      console.log(procuradores)
      selectPautistas(procuradores);
      document.getElementById("pautista").value = procurador.nome;
    }).catch(error => console.error(error)); 
    
  }).catch(error => console.error(error));

};

$('#salvar-editado').click( function () {
    // pautaJson.processo = document.querySelector('#processo').value.trim();
    // pautaJson.data = document.querySelector('#data-pauta').value;
    // pautaJson.hora = document.querySelector('#hora-pauta').value.trim();
    // pautaJson.sala = document.querySelector('#sala-pauta').value.trim();
    // pautaJson.processo = document.querySelector('#processo').value.trim();
    // pautaJson.nomeParte = document.querySelector('#nome-parte').value.trim();
    // pautaJson.cpf = document.querySelector('#cpf-pauta').value.trim();
    // pautaJson.nomeAdvogado = document.querySelector('#nome-advogado').value.trim();
    // pautaJson.objeto = document.querySelector('#objeto').value.trim();
    // var vara = document.getElementById('vara');
    // pautaJson.vara = vara.options[vara.selectedIndex].value;
    // var tipo = document.getElementById('tipo');
    //pautaJson.tipo = tipo.options[tipo.selectedIndex].value;
    var selectPautista = document.getElementById('pautista');
    selectPautista = selectPautista.options[selectPautista.selectedIndex].value;

    var pautista = procuradores.filter(item =>  item.nome == selectPautista);
    pautaJson.pautista = pautista[0];
    pautaJson.procuradorDto = pautista[0];

    if(processo == ""){
        console.log("Processo não pode ser nulo")
    }else{
     // pautaJson.procurador = pautistaId;
        editar(pautaJson.procuradorDto.id);
    }

});

function editar(pautistaId){

  axios.get(baseURL + 'pauta/').then(response => {
    pautas = response.data;
    processo = sessionStorage.getItem('processo'); 
    pautaJson = pautas.filter(item =>  item.processo == processo);
    pautaJson = pautaJson[0];

    console.log(baseURL + "mutirao/" + pautaJson.id + "/" + pautistaId)
      axios.put(baseURL + "mutirao/" + pautaJson.id + "/" + pautistaId).then(response => {
        console.log(response.status)
        //limparCampos();
        window.history.back();
      }).catch(error => console.error(error));
    
  //   var procurador = pautaJson.procurador;
  //   if(procurador)
  //      pautistaId = procurador.id;
  //   if(procurador == null){
  //     pautaJson.procurador = "";
  //   }

  //   pautistaAntigo = procurador;
  //  // pauta.procuradorDto = procurador;

  //   if(pautistaAntigo){
  //     console.log(pauta.procuradorDto);
  //     console.log(pauta);
  //     console.log(url + pauta.id + "/"+pautistaAntigo.id)
  //     axios.put(url + pauta.id + "/"+ pautistaAntigo.id, pauta).then(response => {
  //       console.log(response.status)
  //       //limparCampos();
  //       window.history.back();
  //     }).catch(error => console.error(error));
  //   }

  }).catch(error => console.error(error));
}

function selectPautistas(procuradores){
  //$('#mensagem').html('<span class="mensagem">Aguarde,carregando ...</span>');

     if (procuradores){
      var option;
      $.each(procuradores, function(i, obj){
        option += '<option value="'+obj.nome+'">'+obj.nome+'</option>';
      })
     // $('#mensagem').html('<span class="mensagem">Total de paises encontrados.: '+dados.length+'</span>');
      $('#pautista').html(option).show();
    }
}

function limparCampos(){
  document.getElementById("data-pauta").value = "";
  document.getElementById("hora-pauta").value = "";
  document.getElementById("sala-pauta").value = "";
  document.getElementById("processo").value = "";
  document.getElementById("nome-parte").value = "";
  document.getElementById("cpf-pauta").value = "";
  document.getElementById("nome-advogado").value = "";
  document.getElementById("objeto").value = "";
  document.getElementById("vara").value = "1ª Vara Federal";
  document.getElementById("tipo").value = "Conciliação";
  
  var limpar = "";
  sessionStorage.setItem('processo', limpar); 
}