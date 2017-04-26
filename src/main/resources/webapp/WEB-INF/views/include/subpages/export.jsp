<%@include file="/WEB-INF/views/include/directive/page.jsp"%>
<div class='export-view glow-shadow'>

	<p>Important Warning!!!</p>
	<p>This will export your complete password information to a file.
		This file will have your password information also, therefore, you
		must keep this file safe.</p>
	<p>It is recommended that you delete this file once you are done,
		or keep it secure. Remember, this file will contain all of your
		passwords.</p>
	<p>The only secure file is the 'Password Safe' formatted file. This
		file is dependent on the password that you provide.</p>
	<p>What format would you like to save this file?</p>
	<p>
		<input type="radio" name="file-format" value="pdf">PDF<br>
		<input type="radio" name="file-format" value="xlsx">Excel<br>
		<input type="radio" name="file-format" value="safe">Password
		Safe<br>
	</p>
	<p>Enter a password?</p>
	<p>
		<input type='password' name='password' class='export-password'>
	</p>
	<p>
	<p>Re-Enter the password?</p>
	<p>
		<input type='password' name='repassword' class='export-password'>
	</p>
</div>