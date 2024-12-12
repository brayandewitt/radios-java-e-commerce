async function Register() {
    const user_dto={
        firstname:document.getElementById("firstname").value,
        lastname:document.getElementById("lastname").value,
        email:document.getElementById("email").value,
        password:document.getElementById("password").value,
    };
    
    console.log(user_dto);
    const response = await fetch(
            "Register",
            {
                method:"POST",
                body:JSON.stringify(user_dto),
                headers:{
                    "Content-Type":"application/json"
                }
            }
    );
    
    if(response.ok){
        const json = await response.json();
        if (json.success) {
            window.location="verify.html"
        } else {
            document.getElementById("message").innerHTML = json.content;
        }
        
    }else{
        document.getElementById("message").innerHTML = "Please try again later";
    }
}


