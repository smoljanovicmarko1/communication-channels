import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Post } from '../../../model/Post';

@Component({
  selector: 'app-post-edit',
  templateUrl: './post-edit.component.html',
  styleUrls: ['./post-edit.component.css'],
})
export class PostEditComponent implements OnInit, OnDestroy {
  form: FormGroup;
  subs: Subscription[] = [];

  constructor(
    public dialogRef: MatDialogRef<PostEditComponent>,
    private formBuilder: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: Post
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      title: [this.data.title, Validators.required],
      body: [this.data.body, Validators.required],
    });
  }

  ngOnDestroy(): void {
    this.subs.forEach((value) => value.unsubscribe());
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}
