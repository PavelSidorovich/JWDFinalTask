$(document).ready(function () {
  // getApplications();
  addFilterListener();
  // addViewButtonListener();
});

function addFilterListener() {
  $("#searchInput").on("keyup", function () {
    processFiltering();
  });
}

function getUserBonuses() {
  $.post("/controller?command=get_my_bonuses", function (data) {
    const $cardContainer = $("#bonusCardContainer");

    $cardContainer.text("");
    data.obj.forEach(displayCards);

    function displayCards(driver) {
      const card = createCard(driver);

      $cardContainer.append(card);
      processFiltering();
    }
  }, "json");
}

function processFiltering() {
  const value = $("#searchInput").val().toLowerCase();

  $("#bonusCardContainer>div").filter(function () {
    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
  });
  $("#filterCount").text($("#bonusCardContainer>div:visible").length);
}

function createBonusCard(bonus) {
  const $card = $('<div class="card shadow" style="width: 18rem;"></div>');
  const $cardHeader = createCardHeader(bonus);
  const $cardBody = createCardBody(bonus);

  if (bonus.discount >= 60) {
    $card.addClass("bg-success");
  } else if (bonus.discount >= 40) {
    $card.addClass("bg-primary");
  } else if (bonus.discount >= 15) {
    $card.addClass("bg-warning");
  } else if (bonus.discount >= 0) {
    $card.addClass("bg-danger");
  }
  $card.children()
    .append($cardHeader)
    .append($cardBody);

  return $card;
}

function createCardHeader(bonus) {
  return $('<h4 style="font-size: 100px" class="card-title text-center">' + bonus.discount + '%</h4>');
}

function createCardBody(bonus) {
  return $('<ul class="list-group list-group-flush">'
    + '<li class="list-group-item">Gives a ' + bonus.discount + '% discount when making an order</li>'
    + '<li class="list-group-item">Expires ' + bonus.expires + '</li>'
    + '</ul>');
}
