		var formId; // reference to the main form
		var winId;	// reference to the popup window
		var idASerAlterada;

		// This function calls the popup window.
		// id: O codigo que quem chamou e que vai ter o seu conteudo alterado
		// action:
		// form:
		// target: o nome do bot�o que est� no chamador
		function showPlaceList(id, action, form, target) {
			idASerAlterada = id;
			formId=action.form.id;
			features="height=400,width=600,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes";			
			winId=window.open('/mymon3y/pages/popup_usuarios.jsf','list',features); // open an empty window
//			hform=document.forms[form]; // reference to the hidden form
//			hform.submit();  
			  
			//This is an emulation of the action link being clicked.
//			hform[form+':'+target].value=form+':'+target;
			
			// Copy the current country variable value
			// to the corresponding field of the hidden form.
//			hform[form+":country"].value = getCountry(action.form);
			
			//document.forms["listaDeLogradouros"]["listaDeLogradouros:pesquisa"].click();
			
			//window.blur();
			winId.focus();
			
			// Submit the hidden form. The output will be sent to the just opened window.
		}
		
		
		// This function is called from the popup window 
		// when a user clicks on a state or province from the list.
		// The selected value is copied to a "place" text field
		// in the main form.
		// 
		function updatePlace(place) {
					form=document.forms[formId];
					form[formId+":"+idASerAlterada].value=place;
					winId.close();
		}
		
		
		// This function returns the selected value 
		// from the drop down list.
		//
		function getCountry (form) {
			field = form[form.id+":country"];
			return field.value;
		}
	
		// This function cleans up the "place" text field.
		//  
		function resetTextField(form, field) {
			fieldName=form.id+":"+field;
			form[fieldName].value="";
		}