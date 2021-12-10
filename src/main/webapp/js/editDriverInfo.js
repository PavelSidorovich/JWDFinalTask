$(document).ready(function () {
  addFileButtonListener();
  addFileChangeListener();
});

function addFileButtonListener() {
  $(document).on("click", ".browse", function () {
    let file = $(this).parents().find(".file");
    file.trigger("click");
  });
}

function addFileChangeListener(){
  $('input[type="file"]').change(function (e) {
    let fileName = e.target.files[0].name;
    $("#carPhotoInput").val(fileName);

    let reader = new FileReader();
    reader.onload = function (e) {
      // get loaded data and render thumbnail.
      document.getElementById("taxiPhoto").src = e.target.result;
    };
    // read the image file as a data URL.
    reader.readAsDataURL(this.files[0]);
  });
}
