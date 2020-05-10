$(document).ready(function() {
	$(".row").hide();
	$('#parkOptions').change(function()  {
		var s = document.getElementById("parkOptions");
		var parkId = s.options[s.selectedIndex].value;
		clearParks();
		loadParks(parkId);
                loadAlerts(parkId);
                loadArticles(parkId);
                $(".selectMessage").hide();
                $(".row").show();
	});
	
});

function loadParks(parkId) {
        var getPark = 'https://developer.nps.gov/api/v1/parks?parkCode=acad&api_key=jOd1aBaW2Nzw1QwVM6i5DYbah3xcN4gR05Cr8d7A&fields=entranceFees,images';
	if(parkId !== '') {
		getPark = 'https://developer.nps.gov/api/v1/parks?parkCode=' + parkId + '&api_key=jOd1aBaW2Nzw1QwVM6i5DYbah3xcN4gR05Cr8d7A&fields=entranceFees,images';
	}
	$.ajax({
		type: 'GET',
		url: getPark,
		success: function(data, status) {
			$("#parkName").text(data.data[0].fullName);
			$(".descriptionHere").append(`<p style="margin-left: 5px; font-size:18px; font-weight: normal; text-align:left;">` + data.data[0].description + `</p>`);
			$(".descriptionHere").append(`<p style="margin-left: 5px; font-size:18px; font-weight: normal; text-align:left;">` + data.data[0].weatherInfo + `</p>`);
			for(var i=0; i<data.data[0].entranceFees.length; i++)	{
				var moneyPrice = parseFloat(data.data[0].entranceFees[i].cost);
				moneyPrice = moneyPrice.toFixed(2);
				$('.priceItems').append(`<div class="card" style="float:left">
												<div class="card-wrapper">
													<div class="card-box">
														<p class="cost" style="font-size:15px; margin: 0;">$` + moneyPrice + `</p>
														<p class="title" style="font-size:12px; margin: 0;">` + data.data[0].entranceFees[i].title + `</p>
														<p class="description" style="font-size:10px; margin: 0; text-align: left;">` + data.data[0].entranceFees[i].description + `</p>
													</div>
												</div>
											</div>`
										);
			}
			var totalItems = 3;
			if(data.data[0].images.length < totalItems) {
				totalItems = data.data[0].images.length;
			}
			var firstItem = true;
			for(var i=0; i<totalItems; i++)	{
				if(firstItem) {
				$('.carousel-inner').append(`<div class="carousel-item active">
												<img class="item" style="overflow:hidden" src="` + data.data[0].images[i].url + `" alt="Temp 1">
											</div>`
										);
					firstItem = false;
				} else {
					$('.carousel-inner').append(`<div class="carousel-item">
													<img class="item" style="overflow:hidden" src="` + data.data[0].images[i].url + `" alt="Temp 2">
												</div>`
											);
				}
			}
			
		},
		error: function() {
			$('#errorMessages')
				.append($('<li>')
				.attr({class: 'list-group-item list-group-item-danger'})
				.text('Error calling web service. Please try again later.'));
		}
	});
};

function loadAlerts(parkId) {
        var getPark = 'https://developer.nps.gov/api/v1/alerts?parkcode=acad&api_key=jOd1aBaW2Nzw1QwVM6i5DYbah3xcN4gR05Cr8d7A';
	if(parkId !== '') {
		getPark = 'https://developer.nps.gov/api/v1/alerts?parkcode=' + parkId + '&api_key=jOd1aBaW2Nzw1QwVM6i5DYbah3xcN4gR05Cr8d7A';
	}
	$.ajax({
		type: 'GET',
		url: getPark,
		success: function(data, status) {
			var totalItems = 5;
			if(totalItems > data.total) {
				totalItems = data.total;
			}
                        if(data.total == 0) {
                            $('.alertItems').append(`<div class="card" style="float:left">
										<div class="card-wrapper">
											<div class="card-box">
												<p class="title" style="font-size:15px; margin: 0;">No current alerts!</p>
											</div>
										</div>
									</div>`
									);
                        } else {
			for(var i=(data.total-1); i>=data.total-totalItems; i--)	{
				$('.alertItems').append(`<div class="card" style="float:left">
										<div class="card-wrapper">
											<div class="card-box">
												<p class="title" style="font-size:15px; margin: 0;">` + data.data[i].title + `</p>
												<p class="description" style="font-size:10px; margin: 0; text-align: left;">` + data.data[i].description + `</p>
											</div>
										</div>
									</div>`
									);
			}
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

function loadArticles(parkId) {
    var getPark = 'https://developer.nps.gov/api/v1/articles?parkCode=acad&api_key=jOd1aBaW2Nzw1QwVM6i5DYbah3xcN4gR05Cr8d7A';
    if(parkId !== '') {
            getPark = 'https://developer.nps.gov/api/v1/articles?parkCode=' + parkId + '&api_key=jOd1aBaW2Nzw1QwVM6i5DYbah3xcN4gR05Cr8d7A';
    }
    $.ajax({
            type: 'GET',
            url: getPark,
            success: function(data, status) {
                    var totalItems = data.total;
                    if(totalItems > 50) {
                        totalItems = 50;
                    }
                    var r = Math.floor(Math.random() * totalItems); 
                    console.log(r);
                    if(data.total == 0) {
                        $('.article').append(`<div class="col-md-12">
								<h4>No Articles Currently Available!</h4>
							</div>`
                                                                    );
                    } else if(data.data[r].listingimage.url === "") {
                            $('.article').append(`<div class="col-md-6">
                                                            <div class="col-md-12">
                                                                    <h5>No image available!</h5>
                                                            </div>
                                                    </div>
                                                    <div class="col-md-6" style="float:left">
                                                            <h6>` + data.data[r].title + `</h6>
                                                            <p style="text-align:left; padding-left: 3px;">` + data.data[r].listingdescription + `</p>
                                                            <a class="btn btn-secondary" href=` + data.data[r].url + `>View Full Article</a>
                                                    </div>`
                                                    );
                    } else {
                            $('.article').append(`<div class="col-md-6">
                                                            <div class="col-md-12">
                                                                    <img class="image" src="` + data.data[r].listingimage.url + `"/>
                                                            </div>
                                                    </div>
                                                    <div class="col-md-6" style="float:left">
                                                            <h6 style="padding-left: 3px; padding-right: 3px;">` + data.data[r].title + `</h6>
                                                            <p style="text-align:left; padding-left: 3px; padding-right: 3px;">` + data.data[r].listingdescription + `</p>
                                                            <a class="btn btn-secondary" style="margin-bottom: 5px" href=` + data.data[r].url + `>View Full Article</a>
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

function clearParks() {
    $('.carousel-inner').empty();
    $('.descriptionHere').empty();
    $('.priceItems').empty();
    $('.alertItems').empty();
    $('.article').empty();
}
