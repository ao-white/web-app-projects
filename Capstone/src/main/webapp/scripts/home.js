$(document).ready(function() {
	loadAlerts();
});

function loadAlerts() {
	$.ajax({
		type: 'GET',
		url: 'https://developer.nps.gov/api/v1/alerts?&api_key=jOd1aBaW2Nzw1QwVM6i5DYbah3xcN4gR05Cr8d7A',
		success: function(data, status) {
			for(var i=49; i>44; i--)	{
				$('.alerts').append(`<div class="card" style="float:left" id="` + data.data[i].id + `">
										<div class="card-wrapper">
											<div class="card-box">
												<h5 class="title">` + data.data[i].title + `</h5>
												<p class="description">` + data.data[i].description + `</p>
											</div>
										</div>
									</div>`
									);
			}
		},
		error: function() {
			$('#errorMessages')
				.append($('<li>')
				.attr({class: 'list-group-item list-group-item-danger'})
				.text('Error calling web service. Please try again later.'));
		}
	});
}