console.log("This is script file")


const toggleSidebar =() =>{

    if($(".sidebar").is(":visible"))
    {
        $(".sidebar").css("display","none");
        $(".content").css("margin-left","0%");
    }
    else
    {
        $(".sidebar").css("display","block");
        $(".content").css("margin-left","20%");
    }
}

function deleteBook(id)
{
  swal({
  title: "Are you sure?",
  text: "Once deleted, you will not be able to recover this Book!!!",
  icon: "warning",
  buttons: true,
  dangerMode: true,
  })
.then((willDelete) => {
  if (willDelete) {
    window.location="/user/delete/"+id
  } else {
    swal("Your Book is safe!");
  }
});
	
}


const search=()=>{
	
	
  
  let query=$('#search-input').val();

  if (query=="") {
    $(".search-result").hide();
  } 
  else {
    
   let url=`http://localhost:8282/user/search/${query}`;

   fetch(url)
   .then((response) => { return response.json();})
   
   .then((data) => {
     
    let text =`<div class='list-group'>`;
    

    data.forEach((book)=> {
      text+=`<a href='/user/${book.bid}/book' class='list-group-item list-group-item-action'>${book.name} </a>`
    });
    text+=`</div>`;


    $(".search-result").html(text);
    $(".search-result").show();

   });

  }
};




















