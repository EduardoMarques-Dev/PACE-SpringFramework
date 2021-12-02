// Call the dataTables jQuery plugin
$(document).ready(function() {
  $('#dataTable').DataTable({
    "language": {
      "lengthMenu": "Exibir _MENU_ entrada", 
      "info": "Exibir _START_ a _END_ de _TOTAL_ entradas",
      "search": "Pesquisar",
      "emptyTable": "Sem dados para exibir",
      "paginate":{
        "previous": "Anterior",
        "next": "Próximo",
        "first": "Primeiro",
        "last": "Último"
      }
    }
  });
  $("#tbody-advogado").append("");
});

$(document).ready(function() {
  var table = $('#dataTable').DataTable();

  $('#dataTable tbody').on( 'click', 'tr', function () {
      if ( $(this).hasClass('selected') ) {
          $(this).removeClass('selected');
      }
      else {
          table.$('tr.selected').removeClass('selected');
          $(this).addClass('selected');
      }
  } );
} );

// Call the dataTablePesquisas jQuery plugin
$(document).ready(function() {
  $('#dataTablePesquisa').DataTable({
    "language": {
      "lengthMenu": "Exibir _MENU_ entrada", 
      "info": "Exibir _START_ a _END_ de _TOTAL_ entradas",
      "search": "Pesquisar",
      "emptyTable": "Sem dados para exibir",
      "paginate":{
        "previous": "Anterior",
        "next": "Próximo",
        "first": "Primeiro",
        "last": "Último"
      }
    }
  });
  $("#tbody-advogado").append("");
});

$(document).ready(function() {
  var table = $('#dataTablePesquisa').DataTable();

  $('#dataTablePesquisa tbody').on( 'click', 'tr', function () {
      if ( $(this).hasClass('selected') ) {
          $(this).removeClass('selected');
      }
      else {
          table.$('tr.selected').removeClass('selected');
          $(this).addClass('selected');
      }
  } );
} );




function goBack() {
  window.history.back()
}

function refresh() {
  location.reload()
}
