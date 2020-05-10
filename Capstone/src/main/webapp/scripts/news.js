$(document).ready(function() {
	loadNews('');
	
	$('#parkOptions').change(function()  {
		var s = document.getElementById("parkOptions");
		var parkId = s.options[s.selectedIndex].value;
		clearNews();
		loadNews(parkId);
	});
});

function loadNews(parkId) {
	var getNews = 'https://developer.nps.gov/api/v1/newsreleases?&api_key=jOd1aBaW2Nzw1QwVM6i5DYbah3xcN4gR05Cr8d7A';
	if(parkId !== '') {
                console.log(parkId);
		getNews = 'https://developer.nps.gov/api/v1/newsreleases?parkCode=' + parkId + '&api_key=jOd1aBaW2Nzw1QwVM6i5DYbah3xcN4gR05Cr8d7A';
	}
	$.ajax({
		type: 'GET',
		url: getNews,
		success: function(data, status) {
                    if(data.total == 0) {
                        console.log("EMPTY");
                        $('.card-columns').append(`<div class="card" style="float:left; height: 50px; text-align:center;">
                                                        <div class="card-wrapper">
                                                                <div class="card-box">
                                                                        <h4 class="card-title mbr-fonts-style display-7">No news available currently!</h4>
                                                                </div>
                                                        </div>
                                                </div>`
                                                );
                    } else {
			var totalItems = 12;
                        if(data.total < 12) {
                            totalItems = data.total;
                        }
			for(var i=0; i<totalItems; i++)	{
				var dateString = data.data[i].releasedate;
				var date = dateString.slice(5, 7) + '-' + dateString.slice(8, 10) + '-' + dateString.slice(0, 4);
				$('.card-columns').append(`<div class="card" style="float:left">
											<div class="card-wrapper">
												<div class="card-img">
													<img src="` + data.data[i].image.url + `" media-simple="true">
												</div>
												<div class="card-box">
													<h4 class="card-title mbr-fonts-style display-7">` + data.data[i].title + `</h4>
													<h6>` + date + `</h6>
													<p class="mbr-text mbr-fonts-style display-7">
														` + data.data[i].abstract + `
														<a href="` + data.data[i].url + `">Learn more...</a>
													</p>
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
};

function clearNews() {
	$('.card-columns').empty();
}
