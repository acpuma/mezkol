( function() {
	CKEDITOR.plugins.add( 'addimage',
	{
		init: function( editor )
		{
			editor.addCommand( 'addimage', {
				exec: function( editor ) {
					//var now = new Date();
					//editor.insertHtml( 'The current date and time is: <em>' + now.toString() + '</em>' );
					//$('imageSelectDialogWidget').show();
					//PrimeFaces.widget.ab({s:"pageForm:j_idt76:j_idt80",onco:function(xhr,status,args){PF('imageSelectDialogWidget').show();}});return false;
					//Window.PF('imageSelectDialogWidget').show();
					insertImage=true;
					PrimeFaces.widgets['imageSelectDialogWidget'].show();
					//#{p:widgetVar('imageSelectDialogWidget')}.show();

				}
			});

			editor.ui.addButton( 'addimage',
			{
				label : 'Resim Ekle',
				toolbar : 'insert',
				command : 'addimage',
				icon : this.path + 'icons/addimage.png'
			});

		}
	});
})();



