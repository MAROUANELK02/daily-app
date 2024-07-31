import {Component, OnInit} from '@angular/core';
import {TasksRepositoryService} from "../services/tasks.repository.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Task} from "../models/task.model";
import {Router} from "@angular/router";
import {AppStateService} from "../services/app-state.service";

@Component({
  selector: 'app-add-task',
  templateUrl: './add-task.component.html',
  styleUrl: './add-task.component.css'
})
export class AddTaskComponent implements OnInit{
  quillConfiguration = {
    toolbar: [
      ['bold', 'italic', 'underline', 'strike'],
      ['blockquote', 'code-block'],
      [{ list: 'ordered' }, { list: 'bullet' }],
      [{ header: [1, 2, 3, 4, 5, 6, false] }],
      [{ color: [] }, { background: [] }],
      ['link'],
      ['clean'],
    ],
  }
  form!: FormGroup;

  constructor(private taskService : TasksRepositoryService, private fb : FormBuilder,
              private router : Router,
              private appState : AppStateService) {
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      priority: ['', Validators.required]
    });
    if(this.appState.authState.isAuthenticated) {
      this.appState.getCurrentUserImage();
    }
  }

  handleCreate() {
    if(this.form.valid) {
      let task : Task = new Task();
      task.title = this.form.value.title;
      task.description = this.form.value.description;
      task.priority = this.form.value.priority;
      this.taskService.createTask(task);
      this.form.reset();
      this.router.navigateByUrl("/colleagues");
    }
  }
}
