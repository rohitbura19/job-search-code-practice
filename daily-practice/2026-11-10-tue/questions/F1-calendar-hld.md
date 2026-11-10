# F1 - Calendar: events, invites and free/busy (HLD, 12 min out loud + 5 min review)

Design the scheduling core of a calendar product (think Google Calendar): create events with attendees, accept/decline, recurring events, "find a time that works for everyone", and show a user's week view.

Scale hints: 500M users, 10 events per user per week on average, week-view reads about 20x more frequent than writes, free/busy lookups for up to 50 attendees, attendees in different time zones.

Cover: requirements, APIs, data model (how a recurring event is stored versus expanded; exceptions to one occurrence; time zones and DST), conflict detection and free/busy computation, sharding (by user), fan-out of an invite to attendees' calendars, notifications/reminders, caching of week views, failure modes and trade-offs.
