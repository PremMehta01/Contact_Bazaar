// toggle sidebar on click
const toggleSidebar = () => {
    if($(".sidebar").is(":visible")){
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");
    }else{
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");
    }
}


// popup menu while deleting any contact
function deleteContact(id){
    swal({
      title: "Are you sure?",
      text: "Once deleted, you will not be able to recover this contact!",
      icon: "warning",
      buttons: true,
      dangerMode: true,
    })
    .then((willDelete) => {
      if (willDelete) {
        window.location="/user/delete/" + id;
      } else {
        swal("Your contact is safe!");
      }
    });
 }


 // search contacts
 const search = () => {
    let query = $("#search-input").val();

    if(query == ""){
        $(".search-result").hide();
    }else{

        let url = `http://localhost:3307/search/${query}`;

        fetch(url).then((response) => {
            return response.json();
        }).then((contacts) => {
            // console.log(contacts);

            let text = `<div class='list-group'>`

            contacts.forEach((contact) => {
                text += `<a href='/user/${contact.id}/contact' class='list-group-item list-group-item-action'> ${contact.name} </a>`
            });

            text += `</div>`;

            $(".search-result").html(text);
            $(".search-result").show();
        });
    }
 }