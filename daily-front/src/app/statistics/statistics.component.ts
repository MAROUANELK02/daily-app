import {Component, OnInit} from '@angular/core';
import Chart from 'chart.js/auto';
import {TasksRepositoryService} from "../services/tasks.repository.service";
import {AppStateService} from "../services/app-state.service";

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrl: './statistics.component.css'
})
export class StatisticsComponent implements OnInit{
  public chart: any;

  constructor(private taskService : TasksRepositoryService,
              private appState : AppStateService) {
  }

  ngOnInit(): void {
    if(this.appState.authState.isAuthenticated) {
      this.appState.getCurrentUserImage();
    }
    this.taskService.fetchCompletedStatistics(this.appState.authState.id).subscribe(
      (next) => this.taskService.fetchInProgressStatistics(this.appState.authState.id).subscribe(
        (next) => this.createChart()
      )
    )
  }

  createChart() {
    const labels = Array.from(this.appState.statistics.keys());
    const data = Array.from(this.appState.statistics.values());
    const inProgressLabels = Array.from(this.appState.inProgressStatistics.keys());
    const inProgressData = Array.from(this.appState.inProgressStatistics.values());

    const allLabels = Array.from(new Set([...labels, ...inProgressLabels])).sort();

    const mappedData = allLabels.map(label => this.appState.statistics.get(label) || 0);
    const mappedInProgressData = allLabels.map(label => this.appState.inProgressStatistics.get(label) || 0);

    this.chart = new Chart("MyChart", {
      type: 'line',
      data: {
        labels: allLabels,
        datasets: [
          {
            label: 'Done',
            data: mappedData,
            backgroundColor: 'blue',
          },
          {
            label: 'In Progress',
            data: mappedInProgressData,
            backgroundColor: 'red',
          }
        ]
      },
      options: {
        aspectRatio: 2.5,
      }
    });
  }
}
