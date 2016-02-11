<p class="website-txt-color" align="center">***</p>
<div align="center">
	<div class="row">
		<div class="col-sm-offset-3 col-sm-5">
			<div class="input-group">
				<span class="input-group-addon" id="basic-addon1">LINK</span> <input
					type="text" class="form-control" aria-describedby="basic-addon1"
					id="urlHolder">
			</div>
		</div>
		<div class="col-sm-1">
			<button type="button" class="btn btn-info" id="send_link">Find
				Images!</button>
		</div>
	</div>
</div>
<div class="row" id="loading">
	<div class="col-sm-offset-5 col-sm-2" align="center">
		<img alt="loading" src="${pageContext.request.contextPath}/static-resources/images/loading.gif" height="75" width="75">
	</div>
</div>
<button type="button" class="btn btn-primary btn-lg"  id = "modalButton" data-toggle="modal" data-target="#myModal">
  Launch demo modal
</button>

<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel" align="center">Images</h4>
      </div>
      <div class="modal-body" id="imageHolder">
        
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>
<script
	src="${pagecontext.request.contextpath}/static-resources/js/home.js"></script>