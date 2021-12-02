import baseURL from '../../ambiente/baseURL.js'

var url = baseURL + 'escala/';

var mutiroes;
var pautas;
var pauta;
var selectVara;
var listaAdvogados = [];
var grupoInt = 3;
var varasComMutirao = [];

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
   "procurador": {}
}

window.onload = function() {
  var option='<option selected>Todas com mutirão</option>';

  $('#vara').html(option).show();
  listarVaras();

  axios.get(baseURL + 'pautas/').then(response => {
    pautas = response.data;
    pautas.forEach(listar);
    
    verificarSeTemEscala(pautas);
    getMutirao();
    advogadoListar()
  }).catch(error => console.error(error));

  axios.get(baseURL + 'procuradores/').then(response => {
    var lista = response.data;
    var procuradores = [];
    lista.forEach(function(procurador){
      
      if (procurador.status == "Ativo") 
      procuradores.push(procurador);
    });
    selectPautistas(procuradores);
  }).catch(error => console.error(error)); 

  axios.get(baseURL + 'advogados/').then(response => {
    listaAdvogados = response.data;
  }).catch(error => console.error(error));
};

function listarVaras(){
  axios.get(baseURL + 'mutiroes/').then(response => {
    var mutirao = response.data;
    muti(mutirao);

    // mutirao =  mutirao.filter(item =>  item.id == idMutirao);
    // mutirao = mutirao[0];
    // if(mutirao){
    //   if(mutirao.status == "COM_ESCALA")
    //     btnGerar.style.display = 'none';
    //   else{
    //     btnGerar.style.display = 'inline';
    //     //btnEditar.style.display = 'none';
    //   }
    // }

    // pautas = pautas.filter(item =>  item.vara == vara);
  console.log("Varas com mutirão");
  console.log(varasComMutirao);

  }).catch(error => console.error(error));
}

function listar(pautas){
  var procurador = pautas.procurador;
  if(procurador == null){
    pautas.procurador = "";
  }else{
    if( procurador.nomeProcurador)
      pautas.procurador = procurador.nomeProcurador;
  }

  pautas.data = formatarData(pautas.data, "-");
  var tabela = $('#dataTable').DataTable();
    tabela.row.add( [
      pautas.data,
      pautas.hora,
      pautas.sala,
      pautas.processo,
      pautas.nomeParte,
      pautas.cpf,
      pautas.objeto,
      pautas.nomeAdvogado,
      pautas.vara,
      pautas.procurador

    ] ).draw( false );

    var tabela = $('#dataTablePesquisa').DataTable();
    tabela.row.add( [
      pautas.data,
      pautas.hora,
      pautas.sala,
      pautas.processo,
      pautas.nomeParte,
      // pautas.cpf,
      // pautas.objeto,
      pautas.nomeAdvogado,
      pautas.vara,
      pautas.procurador

    ] ).draw( false );
}

function advogadoListar(){
  var advogadoDaLista = null;
  
  $('#dataTable > tbody  > tr > td').each(function(index, td) { 
    var nomeAdvogado = $(this).text();
    if(nomeAdvogado){
      advogadoDaLista = listaAdvogados.filter(item => item.nomeAdvogado == nomeAdvogado);
      advogadoDaLista = advogadoDaLista[0];
      // console.log(nomeAdvogado)
      if(advogadoDaLista){
        console.log(advogadoDaLista)
        $(this).css('color', 'red');
      }
    }
  });
}
/////////////UTIL//////////////
function pesquisar(pautaJson){
  var tabela = $('#dataTable').DataTable();
  var pautaDaPesquisa; 
  var procurador = pautaJson.procurador;
  console.log(pautaJson);
  axios.get(baseURL + 'pautas/').then(response => {
    var pautas = response.data;
    pautaDaPesquisa = pautas;

    if(pautaJson.data){
      pautaDaPesquisa = pautaDaPesquisa.filter(item =>  item.data == pautaJson.data);
    }

    if(pautaJson.hora){
      pautaDaPesquisa = pautaDaPesquisa.filter(item =>  item.hora == pautaJson.hora);
    }
    
    if(pautaJson.sala){
      pautaDaPesquisa = pautaDaPesquisa.filter(item =>  item.sala == pautaJson.sala);
    }
    
    if(pautaJson.vara){
      pautaDaPesquisa = pautaDaPesquisa.filter(item =>  item.vara == pautaJson.vara);
    }

    if(procurador.nomeProcurador){
      pautaDaPesquisa = pautaDaPesquisa.filter(item =>  item.procurador.nomeProcurador == procurador.nomeProcurador);
    }

    if(pautaDaPesquisa){
      
      tabela.rows().remove().draw(); 
    
      pautaDaPesquisa.forEach(listar);
    }else{
      tabela.rows().remove().draw();
      pautas.forEach(listar);
    }
    console.log("Status "+response.status);
      
  }).catch(error => console.error(error)); 
}

function gerarPorVara(escala){
  axios.post(url, escala).then(response =>{
      console.log("response", response);
     // location.reload();
     // limparCampos();  
  }).catch(error => console.error(error));
}

function getMutirao(){
  //$('#mensagem').html('<span class="mensagem">Aguarde,carregando ...</span>');
  
  var vara = document.getElementById('vara');
  vara = vara.options[vara.selectedIndex].value; 
  selectVara = vara;

  axios.get(baseURL + 'mutiroes/').then(response => {
    mutiroes = response.data;
    mutiroes = mutiroes.filter(item =>  item.vara == vara);
    
    if(mutiroes){
      listarMutirao(mutiroes);
      exibirBtnGerar();
    }

  }).catch(error => console.error(error));

  // axios.get(baseURL + 'pautas/').then(response => {
  //   pautas = response.data;
  //   pautas = pautas.filter(item =>  item.vara == vara);
  //   if(pautas.length < 1)
  //     pautas = response.data;
  //   listaEspecificaListar(pautas)
    
  // }).catch(error => console.error(error));
    
}

function selectPautistas(procuradores){

  if (procuradores){
    var option = '<option></option>';
    $.each(procuradores, function(i, obj){
      option += '<option value="'+obj.nomeProcurador+'">'+obj.nomeProcurador+'</option>';
    })
    $('#pautista').html(option).show();
  }
}

function exibirPorMutirao(){
  var tabela = $('#dataTable').DataTable();
  var idMutirao = document.getElementById('mutirao');
  var pautasPorMutirao = [];
  idMutirao = idMutirao.options[idMutirao.selectedIndex].value;  

  axios.get(baseURL + 'pautas/').then(response => {
    pautas = response.data;
    pautas = pautas.filter(item =>  item.vara == vara);
    if(pautas.length > 0){
      pautas.forEach(function(pauta){
        var mutirao = pauta.mutirao;
        if(mutirao.id == idMutirao)
           pautasPorMutirao.push(pauta);
      });
    }else{
      pautas = response.data;
      
      tabela.rows().remove().draw(); 
      pautas.forEach(listar);
    }
    tabela.rows().remove().draw(); 
    pautasPorMutirao.forEach(listar);
    console.log(pautasPorMutirao);
  }).catch(error => console.error(error));


  var vara = document.getElementById('vara');
  vara = vara.options[vara.selectedIndex].value; 
  selectVara = vara;

  axios.get(baseURL + 'mutiroes/').then(response => {
    mutiroes = response.data;
    mutiroes = mutiroes.filter(item =>  item.vara == vara);
    
    if(mutiroes){
      listarMutirao(mutiroes);
      exibirBtnGerar();
    }

  }).catch(error => console.error(error));
    
}

function muti(mutiroes){
  console.log(mutiroes)
  var option = '';
  
  var naoTem =  true;
  varasComMutirao[0] = mutiroes[0].vara;

  $.each(mutiroes, function(i, obj){
    console.log(i)
    if(i == 0){
      option += '<option value="'+obj.vara+'">'+obj.vara+'</option>';
    }
    varasComMutirao.forEach(function(vara){
      if(obj.vara == vara){
        naoTem = false;
      }else{
        naoTem = true;
      }
    });
    if(naoTem){
      varasComMutirao.push(obj.vara);
      option += '<option value="'+obj.vara+'">'+obj.vara+'</option>';
    }
  }) 
  $('#vara').html(option).show(); 
}

function listarMutirao(mutiroes){
  if (mutiroes.length > 0){
    var option='';
    $.each(mutiroes, function(i, obj){
      option += '<option value="'+obj.id+'">'+ formatarData(obj.dataInicial, "/") +' a '+ formatarData(obj.dataFinal, "/") +'</option>';
    })
    //mutiroes.forEach(exibirMutirao);
    
  }else{
    option = '<option selected>Vara selecionada sem Mutirão</option>';
  }
       // $('#mensagem').html('<span class="mensagem">Total de paises encontrados.: '+dados.length+'</span>');
  $('#mutirao').html(option).show();

  var mutirao = document.getElementById('mutirao');
  if(mutirao){  
    mutirao = mutirao.options[mutirao.selectedIndex].value; 
    selectVara = mutirao;
    //listarMutirao(mutirao);
  }
}

function listarVaraComPauta(mutiroes){
  if (mutiroes.length > 0){
    var option='<option selected>Todas com mutirão</option>';
    $.each(mutiroes, function(i, obj){
      option += '<option value="'+obj.id+'">'+ formatarData(obj.dataInicial, "/") +'</option>';
    })
    //mutiroes.forEach(exibirMutirao);
    
  }else{
    option = '<option selected>Sem Mutirão</option>';
  }
       // $('#mensagem').html('<span class="mensagem">Total de paises encontrados.: '+dados.length+'</span>');
  $('#mutirao').html(option).show();

  var mutirao = document.getElementById('mutirao');
  if(mutirao){  
    mutirao = mutirao.options[mutirao.selectedIndex].value; 
    selectVara = mutirao;
    //listarMutirao(mutirao);
  }
}

function formatarData(LocalDate, char){
  if(LocalDate !== null){
    if(LocalDate.indexOf("-") == 4 && LocalDate.length == 10){
      LocalDate= LocalDate.substring(8, 10)+char+LocalDate.substring(5, 7)+char+LocalDate.substring(0, 4);
    }
    if(LocalDate.indexOf("-") == 2 && LocalDate.length == 10){
      LocalDate= LocalDate.substring(6, 10)+char+LocalDate.substring(3, 5)+char+LocalDate.substring(0, 2);
    }
  }
  
  return LocalDate;
}

function exibirBtnGerar(){
  //var btnEditar = document.querySelector('.btnEditar');
  var btnGerar = document.querySelector('.btnGerar');
  var idMutirao = document.getElementById('mutirao');
  if(idMutirao){  
    idMutirao = idMutirao.options[idMutirao.selectedIndex].value;
   
    axios.get(baseURL + 'mutiroes/').then(response => {
      var mutirao = response.data;

      mutirao =  mutirao.filter(item =>  item.id == idMutirao);
      mutirao = mutirao[0];
      if(mutirao){
        if(mutirao.status == "COM_ESCALA")
          btnGerar.style.display = 'none';
        else{
          btnGerar.style.display = 'inline';
          //btnEditar.style.display = 'none';
        }
      }
  
    }).catch(error => console.error(error));
  }
}

function verificarSeTemEscala(pautas){
  var vara = document.getElementById('vara');
  vara = vara.options[vara.selectedIndex].value;

  axios.get(baseURL + 'pautas/').then(response => {
    pautas = response.data;
    pautas = pautas.filter(item =>  item.vara == vara);
    //console.log(pautas);
    if(pautas.length < 1){
      pautas = response.data;
    }
    else{   
      pautas.forEach(function(pauta){
        if(!pauta.mutirao){
          console.log("pauta sem mutirão");
        }else{
          if(!pauta.procurador){
            // console.log("pauta sem pautista")

            var btnGerar = document.querySelector('.btnGerar');
            btnGerar.style.display = 'inline';
          }
        }
      });
      
    }

    
  }).catch(error => console.error(error));
}

function listaEspecificaListar(pautasEspecificas){
  var tabela = $('#dataTable').DataTable();
  tabela.rows().remove().draw(); 
  pautasEspecificas.forEach(listar);
}

function limparCampos(){
  document.getElementById("vara").value = "1ª Vara Federal";
  getMutirao();
}

//////////BOTÕES////////////
 


$('#gerar').on( 'click', function () {
  var idMutirao = document.getElementById('mutirao');
  idMutirao = idMutirao.options[idMutirao.selectedIndex].value; 

  var grupo = document.getElementById('grupo');
  grupo = grupo.options[grupo.selectedIndex].value; 
  if(grupo == 'Preposto')
    grupoInt = 2

  if(grupo == 'Procurador')
    grupoInt = 1
  grupo = grupoInt

  if(idMutirao != "Vara selecionada sem Mutirão"){
    var pautasPorMutirao= [];
    var vara = document.getElementById('vara');
    vara = vara.options[vara.selectedIndex].value; 
     axios.get(baseURL + 'pautas/').then(response => {
      pautas = response.data;
      pautas = pautas.filter(item =>  item.vara == vara);
      console.log(pautas);
     });
     console.log(idMutirao+'/'+grupo)
    axios.post(baseURL + 'mutiroes/'+idMutirao+'/' + grupo).then(response => {
      exibirBtnGerar();
      exibirPorMutirao();
    }).catch(error => console.error(error));  
  }else{
    var vara = document.getElementById('vara');
    vara = vara.options[vara.selectedIndex].value;
    escalaJson.vara = vara;
    console.log(vara)
    gerarPorVara(escalaJson); 
  }

});

$('#vara').on( 'click', function () {
  var vara = document.getElementById('vara');
  vara = vara.options[vara.selectedIndex].value;

  axios.get(baseURL + 'pautas/').then(response => {
    pautas = response.data;
    pautas = pautas.filter(item =>  item.vara == vara);
    //console.log(pautas);
    if(pautas.length < 1)
      pautas = response.data;
    listaEspecificaListar(pautas);
    verificarSeTemEscala(pautas);
  }).catch(error => console.error(error));

  getMutirao();
});

$('#mutirao').on( 'click', function () {
  var tabela = $('#dataTable').DataTable();
  var idMutirao = document.getElementById('mutirao');

  idMutirao = idMutirao.options[idMutirao.selectedIndex].value;
  
  exibirBtnGerar();
   // pautas = pautas.filter(item =>  item.vara == vara); 
    var pautasPorMutirao =[];
    pautas.forEach(function(pauta){
      var mutirao = pauta.mutirao;
      pautaJson.data = pauta.data;
      pautaJson.hora = pauta.hora;
      pautaJson.sala = pauta.sala;
      pautaJson.processo = pauta.processo;
      pautaJson.nomeParte = pauta.nomeParte;
      pautaJson.cpf = pauta.cpf;
      pautaJson.nomeAdvogado = pauta.nomeAdvogado;
      pautaJson.objeto = pauta.objeto;
      pautaJson.procurador = pauta.procurador;
      if(mutirao.id == idMutirao)
         pautasPorMutirao.push(pauta);
    });
    //console.log(pautasPorMutirao)
    if(idMutirao != "Vara selecionada sem Mutirão"){
      
      tabela.rows().remove().draw(); 
      pautasPorMutirao.forEach(listar);
    }
   // 
  
});

$('#editarMutirao').click( function () {

  
});

$('#editar').click( function () {
  var table = $('#dataTable').DataTable();
  pauta = table.row('.selected').data();  
  if(pauta)
    var processo = pauta[3];

  sessionStorage.setItem('processo', processo);
});

$('#pesquisar').click( function () { 

  pautaJson.data = document.querySelector('#data').value.trim();
  pautaJson.hora = document.querySelector('#hora').value.trim();
  pautaJson.sala = document.querySelector('#sala').value.trim();

  pautaJson.vara = document.getElementById('vara').value;
  var procurador = {
    "nomeProcurador": ""
  }

  procurador.nomeProcurador = document.getElementById('pautista').value;
  pautaJson.procurador = procurador;  

  pesquisar(pautaJson);
});

$('#print-escala').click( function () {
  // var conteudo = document.getElementById('card-escala').innerHTML;
  //     tela_impressao = window.open('about:blank');

  // tela_impressao.document.write(conteudo);
  // tela_impressao.window.print();
  // tela_impressao.window.close();
});