#include "oving2.h"
#include <iostream>
#include <thread>
#include <mutex>
#include <vector>
#include <list>

using namespace std;

class Workers {
private:
    const int thread_num;
    vector<thread> threads;
    list<function<void()>> tasks;
    mutex task_mutex;
    condition_variable cv;
    atomic<bool> stopped{false};
    bool stopThread = false;

public:
    Workers(int thread_num) : thread_num(thread_num) {
        if (thread_num < 1)
            cerr << "Invalid number of threads" << endl;
    }

    // Starts all threads
    void start() {
        for (int i = 0; i < thread_num; i++) {
            threads.emplace_back([this] {
                while (true) {
                    function<void()> task;
                    {
                        unique_lock<mutex> lock(task_mutex);

                        while (!stopThread && tasks.empty()) {
                            cv.wait(lock);
                        }

                        if (!tasks.empty()) {
                            task = *tasks.begin();
                            tasks.pop_front();
                        }
                    }

                    if (task)
                        task();
                }
            });
        }
    }

    // Stops all threads and joins
    void stop() {
        stopThread = true;
        cv.notify_all();

        cout << "Joining threads..." << endl;
        for (auto &thread : threads)
            thread.join();
        cout << "Stopped and joined threads" << endl;
    }

    // Adds new task
    void post(const function<void()> &task) {
        {
            unique_lock<mutex> lock(task_mutex);
            tasks.emplace_back(task);
        }
        cv.notify_one();
    }
};


int o2_main() {
    cout << "TDAT2004 - Øving 2" << endl;

    Workers worker_threads(4);
    Workers event_loop(1);

    worker_threads.start(); // Create 4 internal threads
    event_loop.start(); // Create 1 internal thread

    // Task A
    worker_threads.post([] {
        cout << "Task A start" << endl;
        cout << "Task A end" << endl;
    });

    // Might run in parallel with task A
    worker_threads.post([] {
        cout << "Task B start" << endl;
        cout << "Task B end" << endl;

    });

    // Might run in parallel with task A and B
    event_loop.post([] {
        cout << "Task C start" << endl;
        cout << "Task C end" << endl;

    });

    // Will run after task C
    // Might run in parallel with task A and B*/
    event_loop.post([] {
        cout << "Task D start" << endl;
        cout << "Task D end" << endl;

    });

    this_thread::sleep_for(5s);
    worker_threads.stop();
    event_loop.stop();

    return 0;
}

