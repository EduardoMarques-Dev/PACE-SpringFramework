import baseURL from '../../ambiente/baseURL.js'

var url = baseURL + 'escala/';

var procuradores = [];
var procuradores = [];
var totalDePautas = 0;
var varaAtual = 'TODAS AS VARAS';
var selectVara = 'TODAS'
var tablePautista = document.getElementById("tablePautista");

var pautistaJson = {
  "nomeProcurador": "",
  "status": "",
  "dataInicial":"",
  "dataFinal":"",
  "grupo":"",
  "saldo": ""
}

window.onload = function() {
  pesquisar();
  getMutirao();
  pegarProcuradores();
};

function addRowTablePautista(pautista, saldo){
  console.log(pautista+' '+saldo)
  var totalLinhas = tablePautista.rows.length;
  var linha = tablePautista.insertRow(totalLinhas);

  var cellPautista = linha.insertCell(0);
  var cellSaldo = linha.insertCell(1);

  cellPautista.innerHTML = pautista;
  cellSaldo.innerHTML = saldo;
}

function pegarProcuradores(){
  var vara = document.getElementById('vara');
  varaAtual = vara.options[vara.selectedIndex].value;

  axios.get(baseURL + 'procuradores/').then(response => {
    var lista = response.data;
    lista.forEach(function(procurador){
      procurador.saldo = parseInt(procurador.saldo);
      if (procurador.status == "Ativo"){     
      // console.log(procurador.nomeProcurador+' '+procurador.saldo)
        procuradores.push(procurador);
      }
    });

    selectPautistas(procuradores);
    
  exibirTabelaPautista(procuradores, varaAtual);

    // listarTablePautista(procuradores);

  }).catch(error => console.error(error)); 
}

function listarTablePautista(lista){
  lista.forEach(function(procurador){
    addRowTablePautista(procurador.nomeProcurador, procurador.saldo);
  });
}

function somarTotalDePeso(lista){
  lista.forEach(function(procurador){
    totalDePautas = totalDePautas + procurador.saldo;
  });
  return totalDePautas;
}

function selectPautistas(procuradores){
  //$('#mensagem').html('<span class="mensagem">Aguarde,carregando ...</span>');

     if (procuradores){
      var option = '<option selected>TODOS</option>';
      $.each(procuradores, function(i, obj){
        option += '<option value="'+obj.id+'">'+obj.nomeProcurador+'</option>';
      })
    //  $('#mensagem').html('<span class="mensagem">Total  encontrados.: '+obj.id+'</span>');
      $('#pautista').html(option).show();
    }
}

function atualizarTablePautitas(listaDePautas, vara){
  var pautistas = [];
  var add = true;
  pautistas.push(listaDePautas[0].procurador)

  listaDePautas.forEach(function(pauta){
    if(pauta.procurador){
   
      add = true;
      pautistas.forEach(function(pautista){
        if(pautista){
          if(pauta.procurador.nomeProcurador == pautista.nomeProcurador){
            add = false;
          }
        }
      });
      
      if(add == true){
        pautistas.push(pauta.procurador)
      }
    }
  });
  if(vara != 'TODAS AS VARAS'){
    exibirTabelaPautista(pautistas, vara);
  }else{
    exibirTabelaPautista(procuradores, vara);
  }
    // removerProcuradorTabela(pautistas);
    // listarTablePautista(pautistas);

    // addRowTablePautista('TODOS', totalDePautas);
    // if(pautistaJson.nomeProcurador){
    //   pautistaDaPesquisa = pautistas.filter(item =>  item.nomeProcurador == pautistaJson.nomeProcurador);
    // }
    // if(listaDePautas[0].vara == 'TODAS AS VARAS'){
    //   console.log('oi')
    // }else{
    //   console.log(listaDePautas[0].vara)
    // }
    // listarTablePautista(procuradores)
}


function exibirTabelaPautista(procuradores, vara){

  if(vara = 'TODAS AS VARAS'){
    var theadPautista = '<tr class="table-success"> '+
            '<th style="padding-right: 20px;" scope="col">'+vara+'</th>'+
            '<th scope="col"> QUANTIDADE DE AUDIÊNCIAS</th>'+
          '</tr>';

    var tbody;
    $('#theadPautista').html(theadPautista).show();
    axios.get(baseURL + 'pautas/').then(response => {
      totalDePautas =  response.data.length;
      tbody = '<tr> <td>TODOS</td> <td>'+ totalDePautas +'</td> </tr>';
      $.each(procuradores, function(i, obj){
        if(obj){
          tbody += '<tr> <td>'+obj.nomeProcurador+'</td> <td>'+ obj.saldo +'</td> </tr>';
        }
      })
      $('#tbodyPautista').html(tbody).show();
    }).catch(error => console.error(error));
  }else{
    pegarProcuradoresPorVar(vara)
  }

  // pegarProcuradoresPorVara(vara);
}

function removerProcuradorTabela(procurador){
  for(var i = 1; i < tablePautista.rows.length; i++){
    if(procurador){
    tablePautista.deleteRow(i);
      return;
    }
  }
  // console.log(procurador)
  // var totalLinhas = tablePautista.rows.length;
  // var linha = tablePautista.insertRow(totalLinhas);

  // var cellPautista = linha.insertCell(0);
  // var cellSaldo = linha.insertCell(1);

  // cellPautista.innerHTML = pautista;
  // cellSaldo.innerHTML = saldo;
}

function pegarProcuradoresPorVara(vara, pautas){
  var delProcurador = true;
  var lista;
  var index = 0;
  var novaListaProcuradores = [];
  var listaPautas = [];
  var filtro = '2/0/0/0';
  if(vara != 'TODAS AS VARAS'){
    filtro = '2/'+vara+'/0/0'; 
  }
  axios.get(baseURL + 'procuradores/').then(response => {
    lista = response.data;
  }).catch(error => console.error(error));
console.log(vara)
  axios.get(baseURL + 'pautas/' + filtro).then(response => {
    listaPautas = response.data;
    console.log(listaPautas.length)
// Começando na posição do índice 2, remova um elemento

    console.log('Lista de pautas da pesquisa')
    console.log(listaPautas);

    $.each(listaPautas, function(i, pauta){
          
      delProcurador = false;
      console.log(pauta.procurador)
      if(i == 0){
        delProcurador = true;
        novaListaProcuradores.push(pauta.procurador)
      }
      console.log(pauta)
      lista.forEach(function(procurador){
      
        if(pauta.procurador != null){
          if((pauta.procurador.id == procurador.id) && (delProcurador == false)){
            delProcurador = true;
            novaListaProcuradores.push(pauta.procurador)
            
            // removerProcuradorTabela(pauta.procurador)
          }
        }
        
        // console.log(index)
        index++;
      });
    })

console.log('Novos pautistas TAM: '+ novaListaProcuradores.length)
// console.log(novaListaProcuradores);
}).catch(error => console.error(error)); 


    // var tbody;

    // $.each(novaListaProcuradore, function(i, obj){
    //   tbody += '<tr> <td>'+obj.nomeProcurador+'</td> <td>'+ obj.saldo +'</td> </tr>';
    // })
    // $('#tbodyPautista').html(tbody).show();
    

    // /pautas/6/Vara Federal Castanhal/2/10
    // {{host}}/pautas/2/1ª Vara Federal/0/0
    // if(pautistaJson.nomeProcurador){
    //   pautistaDaPesquisa = pautistas.filter(item =>  item.nomeProcurador == pautistaJson.nomeProcurador);
    // }
    // procuradores.push(pautistaJson);
    
    if(vara == 'TODAS AS VARAS'){
      // lista.forEach(function(procurador){
      
      //   procurador.saldo = parseInt(procurador.saldo);
      //   if (procurador.status == "Ativo") 
      //     procuradores.push(procurador);
      // });
    }else{

    }

    selectPautistas(procuradores);
    // exibirTabelaPautista(procuradores, vara);
    //document.getElementById("pautista").value = procurador.nomeProcurador;
}

//////////// FILTRO /////////////

function pesquisar(vara){
  var filtro = '1/0/0/0';
  var mutiraoSelect = document.getElementById('mutirao');
  mutiraoSelect = mutiraoSelect.options[mutiraoSelect.selectedIndex].value;
  
  var varaSelect = document.getElementById('vara');
  varaSelect = varaSelect.options[varaSelect.selectedIndex].value;

  var pautistaSelect = document.getElementById('pautista');
  pautistaSelect = pautistaSelect.options[pautistaSelect.selectedIndex].value;

  if(varaSelect != 'TODAS AS VARAS'){
    filtro = '2/'+vara+'/0/0';
           
    if (mutiraoSelect != 'TODOS') {
      filtro = '3/'+vara+'/'+mutiraoSelect+'/0';
    }
  }

  if(varaSelect != 'TODAS AS VARAS' && mutiraoSelect != 'TODOS' && pautistaSelect != 'TODOS'){
    if(pautistaSelect != 'undefined'){

      // console.log(pautistaSelect)
      filtro = '4/'+vara+'/'+mutiraoSelect+'/'+pautistaSelect;
    }
  }else if(varaSelect == 'TODAS AS VARAS' && mutiraoSelect == 'TODOS' && pautistaSelect != 'TODOS'){
      if(pautistaSelect != 'undefined'){

        filtro = '5/0/0/'+pautistaSelect;
      }
    }else if(varaSelect != 'TODAS AS VARAS' && mutiraoSelect == 'TODOS' && pautistaSelect != 'TODOS'){
        if(pautistaSelect != 'undefined'){
          filtro = '6/'+varaSelect+'/0/'+pautistaSelect;
        }
      }else if(varaSelect != 'TODAS AS VARAS' && mutiraoSelect != 'TODOS' && pautistaSelect == 'TODOS'){
          filtro = '3/'+vara+'/'+mutiraoSelect+'/0';
        }

  axios.get(baseURL + 'pautas/'+filtro).then(response => {
    $('#totalPautas').html(response.data.length).show(); 
    if(response.data.length > 0){
      atualizarTablePautitas(response.data, varaSelect);
    }
  }).catch(error => console.error(error)); 

  axios.get(baseURL + 'procuradores/').then(response => {
    $('#totalPautista').html(response.data.length).show();
  }).catch(error => console.error(error));

  // atualizarTablePautitas(vara)

// 2 Castanhal     todos             todos
// 3 Castanhal    17 a 28            todos

// 4 Castanhal    17 a 28            Bianca
// 5 todos          todos            Bianca
// 6 Castanhal      todos            Bianca

  // console.log('Pesquisa: '+'pautas/'+filtro)
  
  if(false){
    var procurador = pautaJson.procurador;
    console.log(pautaJson);
    axios.get(baseURL + 'pautas/').then(response => {
      var pautas = response.data;
      pautaDaPesquisa = pautas;
      console.log(pautas.length)
      
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
}


 function getMutirao(selectVara){

  axios.get(baseURL + 'mutiroes/').then(response => {
    var mutiroes = response.data;
    $('#totalMutirao').html(response.data.length).show();
    if(selectVara){

      if(selectVara != 'TODAS AS VARAS'){
        mutiroes = mutiroes.filter(item =>  item.vara == selectVara);
        // console.log(mutiroes)
        if(mutiroes){
          listarMutirao(mutiroes);
        }
      }else{
        var option = '<option selected>TODOS</option>';
        $('#mutirao').html(option).show();
      }
    }

  }).catch(error => console.error(error));
    
}

function listarMutirao(mutiroes){
  if (mutiroes.length > 0){
    var option='<option value="TODOS">TODOS</option>';
    $.each(mutiroes, function(i, obj){
      option += '<option value="'+obj.id+'">'+ formatarData(obj.dataInicial, "/") +' a '+ formatarData(obj.dataFinal, "/") +'</option>';
    })
    //mutiroes.forEach(exibirMutirao);
    
  }else{
    option = '<option selected>TODOS</option>';
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
    }else{
      if(LocalDate.indexOf("-") == 2 && LocalDate.length == 10){
        LocalDate= LocalDate.substring(6, 10)+char+LocalDate.substring(3, 5)+char+LocalDate.substring(0, 2);
      }
    }
  }
  
  return LocalDate;
}

$('#vara').on( 'click', function () {
  
  selectPautistas(procuradores)

  var vara = document.getElementById('vara');
  vara = vara.options[vara.selectedIndex].value; 
  var selectVara = vara;
  // exibirTabelaPautista(procuradores, selectVara);
  pesquisar(vara)
  getMutirao(selectVara);
  var idMutirao = document.getElementById('mutirao');
  if(selectVara == 'TODAS AS VARAS'){
    axios.get(baseURL + 'mutiroes/').then(response => {
      $('#totalMutirao').html(response.data.length).show();
    }).catch(error => console.error(error));
  }else{
    
    axios.get(baseURL + 'mutiroes/').then(response => {
      var mutiroes = response.data;
      if(selectVara){
  
        if(selectVara != 'TODAS AS VARAS'){
          mutiroes = mutiroes.filter(item =>  item.vara == selectVara);         
        $('#totalMutirao').html(mutiroes.length).show();
        }
      }
  
    }).catch(error => console.error(error));
  }
})

$('#mutirao').on( 'click', function () {
  var idMutirao = document.getElementById('mutirao');

  idMutirao = idMutirao.options[idMutirao.selectedIndex].value;

    var vara = document.getElementById('vara');
    vara = vara.options[vara.selectedIndex].value; 
    var selectVara = vara;
    pesquisar(selectVara);
});

$('#pautista').on( 'click', function () {
    var vara = document.getElementById('vara');
    vara = vara.options[vara.selectedIndex].value; 
    var selectVara = vara;
    pesquisar(selectVara);
});
